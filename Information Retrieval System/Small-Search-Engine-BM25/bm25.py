#!/usr/bin/python


import sys
from math import log
import json
import operator

corpus = dict()
new_corpus = dict()
index_list = dict()
doc_token_count = dict()
score_list_per_query=dict()

score_list = []
docidl = []
queries = []
result = []

k1 = 1.2
b = 0.75
k2 = 100
qf = 1
r  = 0
R = 0.0
avdl = 0



def parseQuery(queries_file_name):

    # read queries.txt and store result in queries list
    filename = queries_file_name
    for line in open(filename) :
       query = (line.rstrip().split("\n"))
       queries.append(''.join(query).split())


def computeBM25(n, N, dl, avdl, f, qf, r):
    # compute BM25 for each document ID that contains has the stem word present in the query
    c = float(dl) / float(avdl)
    K = k1 * ((1 - b) + b * c)
    p1 = log(((r + 0.5) / (R - r + 0.5)) / ((n - r + 0.5) / (N - n - R + r + 0.5)))
    p2 = (((k1 + 1) * f) / (K + f) ) * (((k2 + 1) * qf) / (k2 + qf))
    return p1 * p2

def runQuery(index_file_name, max_doc):

    maxdoc = int(max_doc)
    with open(index_file_name) as f:
        try:
            index_and_tokens = json.load(f)
            #index2 = yaml.load(f)
        except ValueError:
            index_and_tokens = []

    index = index_and_tokens[0]
    doc_token_count = index_and_tokens[1]
    s = 0
    for c in doc_token_count.values():
        s += c

    total_tokens = len(doc_token_count)
    # avdl =sum of all values contained in doc_token_count / total count of keys in doc_token_count
    avdl = float (s)/ float(total_tokens)

    for q in queries:
        score_list_per_query = dict()
        # calling for each query in queries
        for term in q:
            # each term is present in index
            if term in index:
                # dict contains {doc_id:count of occurence of term in this doc id, ....}
                doc_id_for_term = index[term]
                # loop for each key:value contained in the index with the term matching a given query
                for d,f in doc_id_for_term.iteritems():
                    # frequency of term in each doc
                    freq = f
                    # count of docs that contain term
                    n = len(doc_id_for_term)
                     # total number of tokens that exist in document d
                    dl = doc_token_count[d]
                    doc_score  = computeBM25(n, total_tokens, dl, avdl, freq, qf, r)
                    if d in score_list_per_query:
                        score_list_per_query[d] += doc_score
                    else:
                        score_list_per_query[d] = doc_score
        score_list.append(score_list_per_query)

    query_id = 1
    counter = 1
    for result in score_list:
        # sorted list of [(doc-id1, bm25score),(doc-id2, bm25score)..........]
        results_sorted = (list (reversed(sorted(result.iteritems(), key=operator.itemgetter(1)))))
        # rank reset
        initial_rank = 1
        for item in results_sorted[:maxdoc]:
                print '{:>1}\t{:>0}\t{:>5}\t{:>2}\t{:>13}\t{:>0}\t'.format(query_id,'Q0',item[0],initial_rank,item[1],'MOHITG')
                initial_rank += 1
        query_id += 1


def main(index_file_name, queries_file_name, max_doc):


    parseQuery(queries_file_name)
    runQuery(index_file_name, max_doc)

if __name__ == '__main__':
    index_file_name = sys.argv[1]
    queries_file_name = sys.argv[2]
    max_doc = sys.argv[3]
    main(index_file_name, queries_file_name, max_doc)


