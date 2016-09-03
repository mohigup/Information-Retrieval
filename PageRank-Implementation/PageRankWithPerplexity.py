import math
from operator import itemgetter

__author__ = 'MOHITGUPTA'

# dictionary to represent the entire directed graph
GRAPH = dict()
# dictionary to represent initial page rank of each page
PR = dict()
# dictionary to represent initial page rank of each page
newPR = dict()
# L(q) is the number of out-links (without duplicates) from page q
L = dict()

# P is the set of all pages; |P| = N
P = list()
# S is the set of sink nodes, i.e., pages that have no out links
S = list()
# M(p) is the set (without duplicates) of pages that link to page p

perplexityL = list()
# d is the PageRank damping/teleportation factor; use d = 0.85 as a fairly typical value
d = 0.85
N = 0


def main():

    filename = raw_input("Please enter file path without space after colon:")
    #filename = "D:/NEU/IR/IR-Assignment-02/wt2g_inlinks.txt"
    print 'Program Execution Started'
    # Build a dictionary denoted as Graph
    for line in open(filename):
        line = line.rstrip()
        pages_with_inlinks = line.split(' ')
        page = pages_with_inlinks[0]
        P.append(page)
        GRAPH[page] = list()
        for i in range(1, len(pages_with_inlinks)):
            inlink = pages_with_inlinks[i]
            GRAPH[page].append(pages_with_inlinks[i])
            if inlink in L:
                outlinkcounter = L[inlink] + 1
                L[inlink] = outlinkcounter
            else:
                L[inlink] = 1

    print "In-link graph populated"
    print "Populated outlinks "
    S.extend(set(P) - set(L.keys()))
    pageRank(GRAPH, S)


# calculate perplexity
def calPreplexity(pr):

    preplex = 0

    for page in pr:
        preplex -= (pr[page] * math.log(pr[page], 2))
    return pow(2, preplex)


def pageRank(GRAPH, S):
    docidPR = []
    counter = 0
    sources_count = 0
    pages_PR_less_than_initial_counter = 0
    perplexity = 0
    i = 2
    sinks_count = len(S)
    N = len(P)
    initial_PR = float(1) / N

    file1 = open('PerplexityValuesAfterConvergence.txt', 'w+')
    # set initial PageRank of all pages Pages that is maintained as dictionary
    for p in GRAPH:
        PR[p] = initial_PR

    print "Starting PageRank"

    # calculate perplexity

    perplexity += calPreplexity(PR)
    file1.write('Perplexity after 1 iteration:  {0}\n'.format(str(perplexity)))
    while isConvergence(perplexity) == False:
        sinkPR = 0
        for p in S:
            sinkPR += PR[p]
        for p in P:
            newPR[p] = (1 - d) / N + (d * sinkPR) / N
            for q in GRAPH[p]:
                newPR[p] += (d * PR[q]) / (L[q])

        for p in P:
            PR[p] = newPR[p]

        perplexity = calPreplexity(PR)
        file1.write('Perplexity after {0} iteration:  {1}\n'.format(str(i), str(perplexity)))

        i += 1
    file1.close()
    print "Completed writing perplexity values to file"

    for page in PR:
        docidPR += [(page, PR[page])]



    file3 = open('All_Pages_PageRanks.txt', 'w+')
    file3.write("page ID" + '\t' + "PageRank" + '\n')
    for docid, r in list(reversed((sorted(docidPR, key=itemgetter(1))))):
        file3.write(str(docid) + '\t' + str(r) + '\n')
    print 'Completed writing Doc ID and page rank values to file'
    file2 = open('Misc Statistics.txt', 'w+')
    file2.write('\n  Top 50 pages by page rank: \n')
    for docid, r in list(reversed((sorted(docidPR, key=itemgetter(1)))[-50:])):
        file2.write(str(docid) + '\t' + str(r) + '\n')
    print 'Completed writing Top 50 pages by page rank to file'
    file2.write('\n  Top 50 pages by in-link count: \n')
    for k in list(reversed(sorted(GRAPH, key=lambda k: len(GRAPH[k])))):
        if counter < 50:
            file2.write(str(k) + '\t' + str(len(GRAPH[k])) + '\n')
            counter += 1
    print 'Completed writing Top 50 pages by in-link count to file'
    for k, v in GRAPH.items():
        if len(v) == 0:
            sources_count = sources_count + 1

    for page in PR:
        if PR[page] < initial_PR:
            pages_PR_less_than_initial_counter = pages_PR_less_than_initial_counter + 1

    #print "Total Pages", N
    #print "sources_count", sources_count
    #print "sinks_count", sinks_count
    #print "pages_PR_less_than_initial_counter", pages_PR_less_than_initial_counter
    #print "Proportion of pages with no in-links", float(sources_count) / N
    #print "Proportion of pages with no out-links ", float(sinks_count) / N
    #print "Proportion of pages whose PageRank is less than their initial, uniform values", float(pages_PR_less_than_initial_counter) / N

    file2.write('\nProportion of pages with no in-links: ' + str(float(sources_count) / N) + "\n");
    file2.write('\nProportion of pages with no out-links: ' + str(float(sinks_count) / N) + "\n");
    file2.write('\nProportion of pages whose PageRank is less than their initial, uniform values: ' + str(
        float(pages_PR_less_than_initial_counter) / N) + "\n");
    file2.close()
    print 'Program Execution Completed'

def isConvergence(perplexity):
    perplexityL.append(int(perplexity))
    if len(perplexityL) == 4:
        if perplexityL[0] == perplexityL[1] and perplexityL[0] == perplexityL[2] and perplexityL[0] == perplexityL[3]:
            return True
        else:
            del perplexityL[0]
            return False
    if len(perplexityL) > 4:
        del perplexityL[0]
    if len(perplexityL) < 4:
        return False


if __name__ == '__main__':
    main()
