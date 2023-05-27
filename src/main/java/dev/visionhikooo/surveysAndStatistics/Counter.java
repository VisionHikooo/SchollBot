package dev.visionhikooo.surveysAndStatistics;

public class Counter {
    int val;
     public Counter(int val) {
         this.val = val;
     }

     public Counter() {
         this(0);
     }

     public void increase() {
         val++;
     }
    public void increase(int val) {
        this.val+= val;
    }


     public void decrease() {
         val--;
     }

     public int getVal() {
         return val;
     }
}
