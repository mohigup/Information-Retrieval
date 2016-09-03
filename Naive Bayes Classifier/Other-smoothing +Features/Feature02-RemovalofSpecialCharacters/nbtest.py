#!/usr/bin/python


import sys
from math import log
import json
import glob
import os
import operator
from collections import OrderedDict
from operator import itemgetter



test_data = {}
predict_data = {}
list_positive_negative = []
POS = []
NEG = []
pos_to_neg = dict()
neg_to_pos = dict()
top_p_n =[]
top_n_p = []


def printtopN(toptwenty):
    count = 0
    for key,value in toptwenty.iteritems():
        count = count+1
        print key,value,'\n'
        if count == 20:
             break


def main(model_file_path, input_dir, predication_file):
#def main():
    #model_file_path = 'model-file-hw6.txt'
    test_file_names = []
    print input_dir
    with open(model_file_path) as f:
        try:
            model_data = json.load(f)
            #index2 = yaml.load(f)
        except ValueError:
            mode_data = []

    #print "Sasd
    positive_term_freq = model_data[1]
    negative_term_freq = model_data[0]

    for key, value in positive_term_freq.iteritems():
        pos_to_neg[key] = log(value) - log(negative_term_freq.get(key))



    for key, value in negative_term_freq.iteritems():
        neg_to_pos[key] = log(value) - log(positive_term_freq.get(key))

    newA= OrderedDict(reversed(sorted(pos_to_neg.items(), key=itemgetter(1))))
    print "A list of the 20 terms with the highest (log) ratio of positive to negative weight.\n"
    printtopN(newA)




    newB= OrderedDict(reversed(sorted(neg_to_pos.items(), key=itemgetter(1))))
    print "A list of the 20 terms with the highest (log) ratio of negative to positive weight \n"
    printtopN(newB)

    file_path = ""
    #test_file_names  = glob.glob('textcat/textcat/dev/pos/*.txt')
    #test_file_names  = glob.glob('textcat/textcat/dev/neg/*.txt')
    #test_file_names  = glob.glob('textcat/textcat/test/*.txt')
    #test_file_names  = glob.glob(input_dir)
    #print test_file_names
    #input_dir = 'textcat/textcat/dev'
    for dirName, subdirList, fileList in os.walk(input_dir):
        for fname in fileList:
            test_file_names.append(dirName+"/"+fname)
    print test_file_names
    for i in test_file_names:
        input_file = open(i,'r')

        test_data[i] = input_file.read().replace('\n',' ')
        #print "test-data",  test_data[i]
        c = 0
        cumulative_probabilityP = 0.0
        cumulative_probabilityN = 0.0
        words = test_data[i].split()
        #print "words\n", words

        ##low = test_data[i].split()
        ##words = zip(*[low[k:] for k in range(2)])


        for k in words:
        ##for l in words:
            # if k in positive_term_freq.keys():
            ##k = l[0]+" "+l[1]
            cumulative_probabilityP += log(positive_term_freq.get(k,1))
            # if k in negative_term_freq.keys():
            cumulative_probabilityN += log(negative_term_freq.get(k,1))
        # for key,value in positive_term_freq.iteritems():
        #     if key in words:
        #         cumulative_probabilityP += log(value)
        #         cumulative_probabilityN += log(negative_term_freq.get(key))


        predict_data[i] = [cumulative_probabilityP,cumulative_probabilityN]
        if cumulative_probabilityP > cumulative_probabilityN:
            POS.append(i)
        else:
            NEG.append(i)

        #print "counter is ", c
        print "POS- LENGTH", len(POS)
        print "NEG- LENGTH", len(NEG)
        print "list length", len(predict_data)
        #print "predict_data", predict_data

    #predication_file = 'predication_file.txt'
    file1 = open(predication_file, 'a')
    file1.write('{:>5}\t{:>5}\t{:>2}\t'.format("file-name","pos-score","neg-score"))
    for key, value in predict_data.iteritems():
        sentence_rev = " ".join(reversed(str(key).split('/')))
        file1.write('{:>5}\t{:>5}\t{:>2}\t'.format(sentence_rev.split()[0],value[0],value[1]))
        file1.write("\n")



if __name__ == '__main__':
     model_file_path = sys.argv[1]
     input_dir = sys.argv[2]
     predication_file = sys.argv[3]
     main(model_file_path, input_dir, predication_file)
    #main()





