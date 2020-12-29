//给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。 
//
// 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。 
//
// 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。 
//
// 示例： 
//
// 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
//输出：7 -> 0 -> 8
//原因：342 + 465 = 807
// 
// Related Topics 链表 数学 
// 👍 5336 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        //pre用来保留运算结果，此时pre的value为0，next为空
        ListNode pre = new ListNode(0);
        //动态生成结果，此时cur当前指针指向我们最新运算的结果
        ListNode cur = pre;
        //进位为0
        int carry = 0;
        //遍历，while循环的判断条件为，l1,l2是否已经遍历完
        while(l1 != null || l2 != null) {
            //下面为空的情况说明，此时已经到末尾了
            int x = l1 == null ? 0 : l1.val;
            int y = l2 == null ? 0 : l2.val;

            //求和，再加上进位（进位代表的是i+1相加得到的进位，加到i上）
            int sum = x + y + carry;

            //得到进位和余位
            carry = sum / 10;
            sum = sum % 10;

            //尾插法保留上一步计算结果，此时的结果为余位，进位保存到carry中进行下一次循环
            cur.next = new ListNode(sum);
            //指向结果的指针后移（尾插法）
            cur = cur.next;

            //l1和l2的移动为正序遍历
            if(l1 != null)
                l1 = l1.next;
            if(l2 != null)
                l2 = l2.next;
        }


        //如果最后一个节点还有进位，那么延长
        if(carry == 1) {
            cur.next = new ListNode(carry);
        }
        //返回头指针
        return pre.next;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
