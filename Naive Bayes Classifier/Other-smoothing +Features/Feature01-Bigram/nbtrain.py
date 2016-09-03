#!/usr/bin/python

from __future__ import division
from math import log
import glob
import json
import re
import time
import sys



def store_file_in_list(path):
    lst = []
    for i in path:
        file_temp = open(i,'r')
        # neg_list.append(neg_file_temp.readlines())
        # lst.append(file_temp.read().replace('\n',' '))
        lst.append(file_temp.read())
    return lst

def store_dict_of_freq(lst):
    words_dict = {}
    for i in lst:

        low = i.split()
        words = zip(*[low[j:] for j in range(2)])
        for k in words:
            bigram = k[0]+" "+k[1]
            words_dict[bigram] = words_dict.get(bigram,0) + 1
    return words_dict

def store_total_words_freq(dict_1,dict_2):
    total_words = {}
    total_words = dict_1.copy()
    for i in dict_2.keys():
        total_words[i] = total_words.get(i,0)+dict_2[i]
    return total_words

def find_total_freq_count(dict,totaldict):
    total_count = 0
    for i in dict.keys():
        if totaldict[i]<5:
            dict.pop(i)
        else:
            total_count += dict[i]
    return total_count,dict

def main(input,output):

    start_loop_time = time.time()   # Start time of the loop which contains all the link
    #file_path = raw_input("Enter the path which contains text_cat data\n")

    neg_list = []
    pos_list = []
    neg_words_dict = {}
    pos_words_dict = {}

    neg_train_file_names  = glob.glob(input+'/neg/*.txt')
    pos_train_file_names  = glob.glob(input+'/pos/*.txt')
    neg_list = store_file_in_list(neg_train_file_names)
    pos_list = store_file_in_list(pos_train_file_names)

    neg_words_dict = store_dict_of_freq(neg_list)
    pos_words_dict = store_dict_of_freq(pos_list)

    total_words = store_total_words_freq(neg_words_dict,pos_words_dict)

    for i in total_words.keys():
        pos_words_dict[i] = pos_words_dict.get(i,0)
        neg_words_dict[i] = neg_words_dict.get(i,0)

    print "total words?", total_words.__len__()
    print "neg_words-> length",neg_words_dict.__len__()
    print "pos_words-> length",pos_words_dict.__len__()
    total_neg_count = 0
    total_pos_count = 0

    total_neg_count,neg_words_dict = find_total_freq_count(neg_words_dict,total_words)
    total_pos_count,pos_words_dict = find_total_freq_count(pos_words_dict,total_words)

    total_words = {}

    total_words = store_total_words_freq(neg_words_dict,pos_words_dict)

    print "neg_words-> length",neg_words_dict.__len__()
    print "pos_words-> length",pos_words_dict.__len__()


    neg_words_prob = {}
    pos_words_prob = {}
    total_words_len = total_words.__len__()
    # print "total words?", total_words_len
    # print "total neg words?", total_neg_count
    # print "total pos words?", total_pos_count

    ################# Normal Smooothing####################################################
    for i in neg_words_dict.keys():
        neg_words_prob[i] = (neg_words_dict[i]+1)/(total_neg_count+total_words_len)

    for i in pos_words_dict.keys():
        pos_words_prob[i] = (pos_words_dict[i]+1)/(total_pos_count+total_words_len)
    ################# Normal Smooothing####################################################

    ################# Jelenick-Mercer Smoothing####################################################
    # lambda_value = 0.5
    # for i in neg_words_dict.keys():
    #     neg_words_prob[i] = ((1-lambda_value)*(neg_words_dict[i]/total_neg_count))+(lambda_value*(total_words[i]/(total_neg_count+total_pos_count)))
    #
    # for i in pos_words_dict.keys():
    #     pos_words_prob[i] = ((1-lambda_value)*(pos_words_dict[i]/total_pos_count))+(lambda_value*(total_words[i]/(total_neg_count+total_pos_count)))
    ################# Jelenick-Mercer Smoothing####################################################

    ################# Dirichlet Smoothing####################################################
    # Mu = 0.39
    # for i in neg_words_dict.keys():
    #     neg_words_prob[i] = ((neg_words_dict[i]+(Mu*(total_words[i]/(total_neg_count+total_pos_count))))/(total_neg_count + Mu))
    #
    # for i in pos_words_dict.keys():
    #     pos_words_prob[i] = ((pos_words_dict[i]+(Mu*(total_words[i]/(total_pos_count+total_pos_count))))/(total_pos_count + Mu))
    ################# Dirichlet Smoothing####################################################

    print "here?",neg_words_prob

    all_words_prob_list = [neg_words_prob,pos_words_prob]
    model_file = open(output,"w")
    json.dump(all_words_prob_list,model_file)

    end_loop_time = time.time()   # End time of the loop which contains all the link

    print "total time taken->",end_loop_time-start_loop_time


if __name__ == '__main__':
    input_dir = sys.argv[1]
    output_model = sys.argv[2]
    main(input_dir, output_model)