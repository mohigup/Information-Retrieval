#!/usr/bin/python


import sys
from math import log
import json
import operator

corpus = dict()
new_corpus = dict()
index_list = dict()
doc_token_count = dict()
score_list_per_query = dict()
# precision=list
# recall=list
score_list = []
docidl = []
queries = []
result = []
relevance = []
doc_ranklist = []
relevant_docs = {'1': [1523, 2080, 2246, 2629, 3127],
                 '2': [115, 1223, 1231, 1551, 1625, 1795, 1807, 1947, 2495, 2579, 2897],
                 '3': [141, 863, 950, 1601, 2266, 2664, 2714, 2973, 3075, 3156, 3175]}
retrieved_docs_sofar = []

precision_at_k = []

precision_list_for_avg = []

k1 = 1.2
b = 0.75
k2 = 100
qf = 1
r = 0
R = 0.0
avdl = 0
relevance_level = 0


def parseQuery(queries_file_name):
    # read queries.txt and store result in queries list
    filename = queries_file_name
    for line in open(filename):
        query = (line.rstrip().split("\n"))
        queries.append(''.join(query).split())


def computeBM25(n, N, dl, avdl, f, qf, r):
    # compute BM25 for each document ID that contains has the stem word present in the query
    c = float(dl) / float(avdl)
    K = k1 * ((1 - b) + b * c)
    p1 = log(((r + 0.5) / (R - r + 0.5)) / ((n - r + 0.5) / (N - n - R + r + 0.5)))
    p2 = (((k1 + 1) * f) / (K + f)) * (((k2 + 1) * qf) / (k2 + qf))
    return p1 * p2


def callDCG(rank, relevance_per_doc):
    DCG = 0
    for i in rank:

        if i == 1:

            DCG = 0


        else:

            DCG += float(relevance_per_doc[i - 1]) / (float(log(i, 2)))

    return (DCG + relevance_per_doc[0])


def callIDCG(rank, rel, relevance_per_doc):
    IDCG = 0
    relevance_IDCG = 0
    for i in rank:
        if i == 1:
            IDCG = 0
            # print "IDCG at "+str(i) +" is ", str(IDCG)
        else:
            if i <= rel:
                relevance_IDCG = 1
                IDCG += float(1) / (log(i, 2))
                # print "IDCG at "+str(i) +" is ", str(float(1)/ (log(i,2))  )
            else:
                relevance_IDCG = 0
                IDCG += 0
                # print "IDCG at "+str(i) +" is ", str( 0 )

    # print "Final IDCG at "+str(i) +" is "+ str(IDCG+1)
    return (IDCG + 1)


def runQuery(index_file_name, max_doc):
    maxdoc = int(max_doc)
    with open(index_file_name) as f:
        try:
            index_and_tokens = json.load(f)
            # index2 = yaml.load(f)
        except ValueError:
            index_and_tokens = []

    index = index_and_tokens[0]
    doc_token_count = index_and_tokens[1]
    s = 0
    for c in doc_token_count.values():
        s += c

    total_tokens = len(doc_token_count)
    # avdl =sum of all values contained in doc_token_count / total count of keys in doc_token_count
    avdl = float(s) / float(total_tokens)

    for q in queries:
        score_list_per_query = dict()
        # calling for each query in queries
        for term in q:
            # each term is present in index
            if term in index:
                # dict contains {doc_id:count of occurence of term in this doc id, ....}
                doc_id_for_term = index[term]
                # loop for each key:value contained in the index with the term matching a given query
                for d, f in doc_id_for_term.iteritems():
                    # frequency of term in each doc
                    freq = f
                    # count of docs that contain term
                    n = len(doc_id_for_term)
                    # total number of tokens that exist in document d
                    dl = doc_token_count[d]
                    doc_score = computeBM25(n, total_tokens, dl, avdl, freq, qf, r)
                    if d in score_list_per_query:
                        score_list_per_query[d] += doc_score
                    else:
                        score_list_per_query[d] = doc_score
        score_list.append(score_list_per_query)

    query_id = 1
    counter = 1
    precision_query = 0
    with open("score_list", 'w') as f:
        json.dump(score_list, f)

    print '{:>1}\t{:>0}\t{:>5}\t{:>2}\t{:>13}\t{:>0}\t{:>5}\t'.format("rank", "doc id", "score", "relevance_level",
                                                                      "precision", "recall", "NDCG")

    for result in score_list[:3]:
        # sorted list of [(doc-id1, bm25score),(doc-id2, bm25score)..........]
        results_sorted = (list(reversed(sorted(result.iteritems(), key=operator.itemgetter(1)))))
        # rank reset
        precision_sum = 0
        initial_rank = 1
        for item in results_sorted[:maxdoc]:

            doc_ranklist.append(initial_rank)
            retrieved_docs_sofar.append(int(item[0]))

            if int(item[0]) in relevant_docs[str(query_id)]:
                relevance_level = 1
                relevance.append(relevance_level)
            else:
                relevance_level = 0
                relevance.append(relevance_level)

            recall_n = len((set(relevant_docs[str(query_id)]) & set(retrieved_docs_sofar)))
            precision_d = len(retrieved_docs_sofar)
            recall_d = len(relevant_docs[str(query_id)])
            recall = float(recall_n) / float(recall_d)
            precision = float(recall_n) / float(precision_d)
            if initial_rank == 20:
                precision_at_k.append(precision)
            if relevance_level == 1:
                precision_list_for_avg.append(precision)

            DCGatP = callDCG(doc_ranklist, relevance)
            # print "DCGatP", DCGatP
            IDCGatP = callIDCG(doc_ranklist, len(relevant_docs[str(query_id)]), relevance)
            # print "IDCGatP", DCGatP
            try:
                NDCG = float(DCGatP) / float(IDCGatP)
            except ZeroDivisionError:
                NDCG = 0

            print '{:>1}\t{:>0}\t{:>5}\t{:>2}\t{:>13}\t{:>0}\t{:>5}\t'.format(initial_rank, item[0], item[1],
                                                                              relevance_level, precision, recall, NDCG)
            initial_rank += 1
        precision_query += sum(precision_list_for_avg) / float(len(relevant_docs[str(query_id)]))

        del retrieved_docs_sofar[:]
        del relevance[:]
        del doc_ranklist[:]
        del precision_list_for_avg[:]

        query_id += 1
        print "jumping to new query"
        # print "len-doc_ranklist",  len(doc_ranklist), "len-relevance", len(relevance)

    MAP = float(precision_query) / float(3)

    print "Mean Average Precision" + str(MAP)
    print "Precision", precision_at_k


def main(index_file_name, queries_file_name, max_doc):
    parseQuery(queries_file_name)
    runQuery(index_file_name, max_doc)


if __name__ == '__main__':
    index_file_name = sys.argv[1]
    queries_file_name = sys.argv[2]
    max_doc = sys.argv[3]
    main(index_file_name, queries_file_name, max_doc)
