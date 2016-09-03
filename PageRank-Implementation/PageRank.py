from operator import add

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
P = set()
# S is the set of sink nodes, i.e., pages that have no out links
S = set()
# M(p) is the set (without duplicates) of pages that link to page p

# d is the PageRank damping/teleportation factor; use d = 0.85 as a fairly typical value
d = 0.85
# N is number of Pages
N = 0
# limit is maximum value uptil the page rank have to be computed
limit = 100

def main():
    filename = raw_input("Please enter file path without space after colon:")
    #filename = "D:/NEU/IR/IR-Assignment-02/six-node-directed-graph.txt"
    directedGraph = open(filename)
    # Build a dictionary denoted as Graph
    for line in directedGraph:
        v = line.split()
        GRAPH[v[0]] = v[1:]

    # Build List of Pages P
    # L number of out-links (without duplicates) for each page p in GRAPH
    # Build a set SinkNodes
    for p in GRAPH.keys():
        P.add(p)
        inlinks = list(reduce(add, GRAPH.itervalues()))
        outlinks_count = inlinks.count(p)
        L[p] = outlinks_count
        if inlinks.__contains__(p) == False:
            S.add(p)
    pageRank(GRAPH,S)

def pageRank(GRAPH,S):

    N = len(P)
    for p in GRAPH:
        PR[p] = float(1) / N

    i = 0
    while i < limit:
        sinkPR = 0
        for p in S:
            sinkPR += PR[p]
        for p in P:
            newPR[p] = (1 - d) / N + (d * sinkPR) / N
            for q in GRAPH[p]:
                newPR[p] += (d * PR[q]) / (L[q])
        for p in P:
            PR[p] = newPR[p]

        i += 1
        # open file to write
        pageRankFile = open('PageRank-6-Vertices.txt', 'a+')


        if i == 1:

            pageRankFile.write("PageRank for Iteration 1" + '\n')

            for k in sorted(PR.iterkeys()):
                pageRankFile.write("{0} {1}".format(str(k), (str(PR[k]))))
                pageRankFile.write("\n")

            pageRankFile.write('\n')

        if i == 9:

            pageRankFile.write("PageRank for Iteration 10" + '\n')
            for k in sorted(PR.iterkeys()):
                pageRankFile.write("{0} {1}".format(str(k), (str(PR[k]))))
                pageRankFile.write("\n")

            pageRankFile.write('\n')

        if i == 99:

            pageRankFile.write("PageRank for Iteration 100" + '\n')
            for k in sorted(PR.iterkeys()):
                pageRankFile.write("{0} {1}".format(str(k), (str(PR[k]))))
                pageRankFile.write("\n")

            pageRankFile.write('\n')

if __name__ == '__main__':
    main()

