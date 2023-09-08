package com.example.musicapp.ui.main.library

import androidx.lifecycle.ViewModel

class LibraryViewModel() : ViewModel() {

    // 13. Roman to Integer
    //MCMXCIV
    fun romanToInt(s:String): Int{
        val newS = s.map {
            when(it){
                'I' -> 1
                'V' -> 5
                'X' -> 10
                'L' -> 50
                'C' -> 100
                'D' -> 500
                'M' -> 1000
                else -> 0
            }
        }
        var result = 0
        var pre = 0
        for(i in newS.size-1 downTo 0){
            val curChar = newS[i]
            if(curChar >= pre){
                result += curChar
            }else{
                result -= curChar
            }
            pre = curChar
        }
        return result
    }

    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////
    fun hammingWeight(n: Int):Int {
      var sum = Integer.toBinaryString(n).replace("0", "").length;
        println(sum)
        return sum
    }
    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////

    fun isOdd(i: Int): Boolean {
        return if(i != 0){i % 2 == 1 || i %2 == -1} else false
    }
    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////

    fun canConstruct(ransomNote: String, magazine: String): Boolean {
        val magazineArray = magazine.toCharArray().toMutableList()
        val noteArray = ransomNote.toCharArray().toMutableList()
        for (i in noteArray) {
            if (!magazineArray.remove(i)) {
                return false
            }
        }
        return true
    }

    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////

    fun fizzBuzz(n: Int): List<String> {
        var list : List<String> = mutableListOf()
        for (i in 1..n){
            var y : String = ""
            if(i%3 == 0 && i%5 == 0){
                y = "FizzBuzz"
            }else if (i%5 == 0){
                y = "Buzz"
            }else if(i%3 == 0 ){
                y = "Fizz"
            }else{
                y = i.toString()
            }
            list = list + y.toString()
        }
        println("list : ${list}")
        return list
    }
    fun fizzBuzz2(n:Int):List<String>{
       return (1..n).map {
            when{
                it%15 == 0 -> "FizzBuzz"
                it % 3 == 0 -> "Fizz"
                it % 5 == 0 -> "Buzz"
                else -> it.toString()
            }
        }
    }

    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////

    fun numberOfSteps(num: Int): Int {
        var number = num
        var count = 0
        while(number != 0){
            if(number%2 == 0){
                number = number / 2
            }else{
                number -= 1
            }
            count = count + 1
        }
        return count
    }
    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////
    fun maximumWealth(accounts: Array<IntArray>): Int {
        var max = 0
        for(i in accounts){
            val sum = i.sum()
            if(sum > max){
                max = sum
            }
        }
        return max
    }

    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////    ///////

    fun twoSum(nums: IntArray, target: Int): IntArray {
        val list : ArrayList<Int> = arrayListOf()
        for(i in nums.indices){
            for(j in 1 until nums.size){
                val sum = nums[i] + nums[j]
                if(sum == target){
                    list.add(i)
                    list.add(j)
                    return list.toIntArray()
                }
            }
        }
        return list.toIntArray()
    }
    fun findMedianSortedArrays(nums1: IntArray, nums2: IntArray): Double {
        val sort = nums1.plus(nums2).sorted()
        val mid = sort.size / 2
        return if(sort.size % 2 == 0)
            (sort[mid] + sort[mid-1]).toDouble() / 2
        else
            sort[mid].toDouble()
    }

    //3. Longest Substring Without Repeating Characters
    fun lengthOfLongestSubstring(s: String): Int {
        var left = 0
        var right = 0
        var max = 0
        var hashSet = mutableSetOf<Char>()
        while (right<s.length){
            if(!hashSet.contains(s[right])){
                hashSet.add(s[right])
                println("right : ${s[right]}")
                right++
                max = hashSet.size.coerceAtLeast(max)
            }else{
                hashSet.remove(s[left])
                println("left : ${s[left]}")
                left++

            }
        }
        return max
    }
  //  5. Longest Palindromic Substring
  fun longestPalindrome(s: String): String {
      var palindrome = ""
      for(i in 0..s.length){
          if(s[i] == s[i+1]){
              palindrome = s[i].toString() + s[i+1].toString()
          }else if(s[i] == s[i+2]){
              palindrome = s[i].toString() + s[i+2].toString()
          }
      }
          return palindrome
  }
}





