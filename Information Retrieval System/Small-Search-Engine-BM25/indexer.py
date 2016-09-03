#!/usr/bin/python


import json
import sys

corpus = dict()
new_corpus = dict()
index_list = dict()
doc_token_count = dict()


def buildindex(in_file,out_file):
    N= 0
    # reading document collection
    with open(in_file) as f:
        text = " ".join(line.strip() for line in f)

    # Parsing the document collection and storing in dictionary
    v = text.split("#")
    for line in v:
        if line !='':
            x = line.split()
            corpus[x[0]]= x[1:]


    # Removing any tokens that contain only the digits 0-9.
    for k in corpus.iterkeys():
        mynewlist = [s for s in corpus[k] if s.isdigit()==False ]
        new_corpus[k] = mynewlist
        doc_token_count[k]= len(mynewlist)



    # building inverted index
    for doc_id in new_corpus:
        for index_term in new_corpus[doc_id]:
            if index_term in index_list:
                if doc_id in index_list[index_term]:
                    index_list[index_term][doc_id] = index_list[index_term][doc_id] + 1
                else:
                    index_list[index_term][doc_id]= 1
            else:
                index_list[index_term] = {doc_id: 1}

    result = index_list, doc_token_count

    # writing inverted index to disk
    with open(out_file, 'w') as f:
        json.dump(result, f)
        #pickle.dump(result, f)




def main(input_file_name, output_file_name):

    buildindex(input_file_name, output_file_name)

if __name__ == '__main__':
    input_file_name = sys.argv[1]
    output_file_name = sys.argv[2]
    main(input_file_name, output_file_name)





