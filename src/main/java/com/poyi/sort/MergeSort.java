package com.poyi.sort;

import java.util.Arrays;

public class MergeSort {

    private static void merge(int[] sortNums, int low, int mid, int high, int[] tmp){
        int i=low, j=mid+1, k=low;
        while (k<=high){
            if(i>mid){
                tmp[k++] = sortNums[j++];
            } else if(j>high){
                tmp[k++] = sortNums[i++];
            } else if(sortNums[i]>sortNums[j]){
                tmp[k++] = sortNums[j++];
            } else{
                tmp[k++] = sortNums[i++];
            }
        }
        for(i=low;i<=high;i++){
            sortNums[i] = tmp[i];
        }
    }

    private static void mergeSort(int[] sortNums, int low, int high, int[] tmp){
        if(low >= high){
            return;
        }
        int mid = low + ((high-low) >> 1);
        mergeSort(sortNums, low, mid, tmp);
        mergeSort(sortNums, mid+1, high, tmp);
        merge(sortNums, low, mid, high, tmp);
    }

    private static void sort(int[] sortNums){
        int[] tmp = new int[sortNums.length];
        mergeSort(sortNums, 0, sortNums.length-1,tmp);
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 7, 4, 5, 3};
        sort(nums);
        System.out.println(Arrays.toString(nums));
    }

}