package com.poyi.sort;

import java.util.Arrays;

public class InsertionSort {

    public static void sort(int[] sortNums){
        for(int i=1;i<sortNums.length;i++){
            for(int j=i;j>0;j--){
                if(sortNums[j]<sortNums[j-1]){
                    swap(sortNums, j, j-1);
                }
            }
        }
    }

    public static void swap(int[] nums, int i, int j){
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 7, 9, 5, 8};
        sort(nums);
        System.out.println(Arrays.toString(nums));
    }

}
