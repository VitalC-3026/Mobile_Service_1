public class Entertainment {
    String name;
    double price;
    Entertainment(String name, double price){
        this.name = name;
        this.price = price;
    }
    public void printInfo(){
        System.out.println("生活服务:"+name+",包月:"+price+"元");
    }
}
