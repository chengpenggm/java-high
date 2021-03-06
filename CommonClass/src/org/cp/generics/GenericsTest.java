package org.cp.generics;

import org.cp.generics.dao.OrderDao;
import org.junit.Test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

/**
 * hello java 泛型
 * create by CP on 2019/8/27 0027.
 */
public class GenericsTest {

    /**
     * 没有泛型的List 测试
     * 1. 添加元素是, 没有严格校验, 导致所有的类型都能添加进来, 有隐患
     * 2. 由于类型都是Object, 在之后的强转处理时候, 可能会报'ClassCastException'
     * 3. 添加一个Integer, 在list中会当成Object对象, 在之后使用中, 又需要转成Interger使用, 操作繁琐
     *
     */
    @Test
    public void notUsedGenerics() {
        ArrayList list = new ArrayList();//new 一个不带泛型的list
        list.add(78);//存放学生的成绩
        list.add(61);
        list.add(53);
//        list.add("Tom");//类型不安全 任何类型都可以添加
//        Iterator iterator = list.iterator();
//        while (iterator.hasNext()) {
//            Object next = iterator.next();
//        }
        for (Object score : list) {
            Integer intScore = (Integer) score;//强转可能出异常
            System.out.println(intScore);
            //在类型强转时, 可能发生类型转换异常
            //java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
        }
    }

    /**
     * 使用泛型 类型安全
     * 1. 添加其他类型, 编译时就发现问题
     * 2. 使用时, 不用强转, 直接使用
     * 3. 在存储时, 直接是Integer类型, 避免后面的类型转换的繁琐操作
     */
    @Test
    public void usedGenerics() {
        //泛型不能是基本数据类型
        ArrayList<Integer> list = new ArrayList<>();
        list.add(78);//存放学生的成绩
        list.add(61);
        list.add(53);
        Iterator<Integer> iterator = list.iterator();// iterator可加泛型, 和容器泛型保持一致
        while (iterator.hasNext()) {
            Integer i = iterator.next();
        }
//        list.add("Tom");//这时编译会报错, 提前发现问题
        for(Integer i : list) {//可直接声明为 Integer类型, 后面不用强转, 直接使用
            System.out.println(i);
        }
    }

    /**
     * map 泛型测试
     * key, value 各有一个泛型  HashMap<String, Integer>
     *
     * 泛型的嵌套  ArrayList<Map<String, Integer>>
     *
     */
    @Test
    public void mapGenericsTest() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Tom", 78);
        map.put("Jerry", 66);
        map.put("Tony", 54);
//        map.put(45, "ok");// 编译报错
        map.forEach((k, v)->{
            System.out.println(k+":"+v);
        });
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            //有泛型, 不用强转
            Map.Entry<String, Integer> item = iterator.next();
            String key = item.getKey();
            Integer value = item.getValue();
        }
        //泛型的嵌套
        ArrayList<Map<String, Integer>> maps = new ArrayList<>();
        maps.add(map);
        System.out.println(maps);
    }

    /**
     * 自定义泛型类测试
     */
    @Test
    public void myGenericsTest() {
        //定义了泛型, 在实例化时要用上
        Order<Double> test = new Order<>(1, "test", 1.2);
        test.setOrderT(4.4);//这里只能放Double类型

        SubOrder subOrder = new SubOrder(1, "subOrder", "String");

        IntegerSubOrder integerSubOrder = new IntegerSubOrder(1, "", 121);
    }

    /**
     * 泛型方法测试
     */
    @Test
    public void genericsMethodTest() {
        SubOrder<String> or = new SubOrder<String>(1, "subOrder", "String");
        BigDecimal bigDecimal = or.valueOf(new BigDecimal(121));
        System.out.println(bigDecimal);

        Integer[] integers = new Integer[]{1,2,3,4,5};
        List<Integer> integerList = Order.copyFromArrayToList(integers);
        System.out.println(integerList);

        Date[] dates = new Date[]{new Date(), new Date(1212121212121212L)};
        List<Date> dateList = Order.copyFromArrayToList(dates);
        System.out.println(dateList);
    }

    @Test
    public void test1() {
        Type<StringBuilder> abc = new Type<>(new StringBuilder("abc"));
//        Type<StringBuilder> def = new Type<>(new StringBuffer("abc"));//这种情况编译不通过
        System.out.println(abc.getType());//abc
        System.out.println(abc.getType().getClass().getName());//java.lang.StringBuilder

        NoType abc1 = new NoType(new StringBuilder("abc"));
        NoType def1 = new NoType(new StringBuffer("def"));
        System.out.println(abc1.getType());//abc
        System.out.println(abc1.getType().getClass().getName());//java.lang.StringBuilder
        System.out.println(def1.getType().getClass().getName());//java.lang.StringBuffer
    }

    /**
     * 赋值与简单通配符
     */
    @Test
    public void genericsClass() {
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<Double> list2 = new ArrayList<>();

        //不同泛型,相同类型的对象间不能相互赋值, 它们相当于两个不同的类型, 并列关系
//        list1 = list2;

        //泛型通配符, 可以赋值
        //但是不能添加除null意外的任何元素
        ArrayList<?> list = null;
        list = list1;
        list = list2;
//        list.add(new Object());
//        list.add(121);
        list.add(null);

    }

    @Test
    public void daoTest() {
        OrderDao orderDao = new OrderDao();
        List<Order> order = orderDao.getAll("order");
    }

    /**
     * 泛型在继承上的体现
     */
    @Test
    public void genericsInInherit() {
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<SubOrder> subOrders = new ArrayList<>();
//        orders = subOrders;//编译失败, orders与subOrders并列, 没有父子关系
        List<Order> list = null;
        list = orders;// 这list与orders是父子关系, List<Order> 是ArrayList<Order> orders的父类

    }

    @Test
    public void wildcardTest() {
        ArrayList<SupOrder> supOrders = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<SubOrder> subOrders = new ArrayList<>();

        ArrayList<?> list = new ArrayList<>();
        ArrayList<? extends Order> list1 = new ArrayList<>();//
        ArrayList<? super Order> list2 = new ArrayList<>();//

        list = subOrders;// ? 可接受任何泛型
        list1 = subOrders;// ? extends Order  可接受Order及其子类
        list2 = orders;//? super Order 可接受Order及其父类

        Order order = list1.get(0);// ? extends Order  可用最高层 Order 多态接收
        Object object = list2.get(0);// ? super Order
    }

}
