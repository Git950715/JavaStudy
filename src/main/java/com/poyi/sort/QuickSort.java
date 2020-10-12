package com.poyi.sort;

import java.util.Arrays;

/**
 * 快速排序
 */
public class QuickSort {

    public static void quickSort(int[] sortNums){
        sort(sortNums, 0, sortNums.length-1);
    }

    public static void sort(int[] sortNums, int low, int high){
        if(low>=high){
           return;
        }
        int partition = partition(sortNums, low, high);
        sort(sortNums, low, partition-1);
        sort(sortNums, partition+1, high);
    }

    public static int partition(int[] sortNums, int low, int high){
        int less = low-1, more=high;
        while(low<more){
            if(sortNums[low]<sortNums[high]){
                swap(sortNums, ++less, low++);
            }else if(sortNums[low]>sortNums[high]){
                swap(sortNums, --more, low);
            }else{
                low++;
            }
        }
        swap(sortNums, more, high);
        return more;
    }

    private static void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 7, 4, 5, 3};
        quickSort(nums);
        System.out.println(Arrays.toString(nums));
    }
}
