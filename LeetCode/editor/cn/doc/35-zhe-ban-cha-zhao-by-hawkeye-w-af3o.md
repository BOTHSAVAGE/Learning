### 解题思路
折半查找
### 代码

```java
class Solution {
    public int searchInsert(int[] nums, int target) {
		if(target<nums[0]) {
			return 0;
		}
		
		int low=0;
		int high=nums.length-1;
		int mid=(low+high)/2;
		while(low<=high) {
			if(nums[mid]==target) {
				return mid;
			}
			else if(nums[mid]>target) {
				high=mid-1;
				mid=(low+high)/2;
			}
			else {
				low=mid+1;
				mid=(low+high)/2;
			}
		}
		return low;
    }
}
```