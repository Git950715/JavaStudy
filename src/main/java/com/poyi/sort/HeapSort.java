package com.poyi.sort;

import java.util.Arrays;

public class HeapSort {

    public static void sort(int[] sortNums){
        int length = sortNums.length;
        //初始化大顶堆
        for(int i=length/2-1;i>=0;i--){
            adjustHeap(sortNums, i, length-1);
        }
        for(int j=length-1;j>0;j--){
            swap(sortNums, 0, j);
            adjustHeap(sortNums, 0, j);
        }

    }

    public static void swap(int[] nums, int i, int j){
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    public static void adjustHeap(int[] sortNums, int start, int end){
        int tmp = sortNums[start];
        for(int k=2*start+1;k<end;k=k*2+1){
            if(k+1<end && sortNums[k]<sortNums[k+1]){
                k++;
            }
            if(sortNums[k]>tmp){
                sortNums[start] = sortNums[k];
                start = k;
            }else{
                break;
            }
        }
        sortNums[start] = tmp;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 7, 9, 5, 8};
        sort(nums);
        System.out.println(Arrays.toString(nums));
    }

}
