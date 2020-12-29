### 解题思路
此处撰写解题思路

### 代码

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode l3 = new ListNode(0);
        ListNode temp1 = l1, temp2 = l2, temp3 = l3;//temp3始终指向已排链表的末尾
        while(temp1 != null && temp2 != null){
            if(temp1.val <= temp2.val){
                temp3.next = temp1;
                temp1 = temp1.next;
            }else{
                temp3.next = temp2;
                temp2 = temp2.next;
            }
            temp3 = temp3.next;
        }
        while(temp1 != null){
            temp3.next = temp1;
            temp1 = temp1.next;
            temp3 = temp3.next;
        }
        while(temp2 != null){
            temp3.next = temp2;
            temp2 = temp2.next;
            temp3 = temp3.next;
        }
        return l3.next;
    }
}
```