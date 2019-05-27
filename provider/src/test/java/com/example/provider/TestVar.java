package com.example.provider;

/**
 * @author shenzm
 * @date 2019-3-14
 * @description 作用
 */
public class TestVar {


    int a = 10;

    Bean bean = new Bean("java", 18);


    public static void main(String[] args) {

        TestVar t = new TestVar();
        t.change(t.a, t.bean);

        System.out.println(t.a);
        System.out.println(t.bean);
    }


    private void change(int a, Bean bean) {
        a = 20;
        bean.setAge(22);
        bean.setName("doc");
    }

    class Bean {
        private String name;
        private int age;

        public Bean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
