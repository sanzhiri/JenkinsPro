package cn.newstrength.wcms.sample.test;

public class test {
    public static void main(String[] args) {
        String path = "D:\\cmsfile\\33333\\1.jpg";
        String[] split = path.split("\\ ");
        String[] split1 = path.split(",");
        System.out.println(split);
        System.out.println(split1);
    }
}
