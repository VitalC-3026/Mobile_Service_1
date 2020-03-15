import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class NormalPackage extends MonthlyPackage implements Text, Call, Surf, DataPack {
    /*NormalPackage(){
        PackageName = "普通计费";
        PackageNumber = 4;
        phone_call = 0;
        data = 0;
        message = 0;
        fee = 0;
    }
     */

    private static MySQL_PackageInfo packUtil = new MySQL_PackageInfo();

    NormalPackage(){
        PackageName = "普通计费";
        PackageNumber = 4;
        try {
            packUtil.ModifyPackageInformation(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printPackage() {
        System.out.println(PackageName);
        System.out.println("通话:0.3元/分钟");
        System.out.println("上网流量:0.3元/MB");
        System.out.println("短信:0.2元/条");
    }
    public void printToText(BufferedWriter bw) throws IOException {
        String[] packageInfo = {"普通计费\n","通话:0.3元/分钟\n","上网流量:0.3元/MB\n", "短信:0.2元/条\n"};
        for(int i = 0; i < packageInfo.length; i++){
            bw.append(packageInfo[i]);
            bw.flush();
        }
    }
    public void call(double CallTime){
        phone_call += CallTime;
        fee = fee + CallTime*0.3;
    }

    @Override
    public double OutOfCall(double CallTime) {
        return -1;
    }

    public void surf(double Data){
        if(data>=pack_data){
            System.out.println("您使用的流量已超过套餐包含的流量，龙龙将按照超出套餐包的价格收费。");
            data += Data;
            extra_data += Data;
            fee += Data*0.1;
        }
        else if(data+Data>=pack_data){
            double temp = data + Data - pack_data;
            System.out.println("您使用的流量已超过套餐包含的流量，龙龙将按照超出套餐包的价格收费。");
            data += Data;
            extra_data += temp;
            fee += temp*0.1;
        }
        else{
            data += Data;
        }
    }

    @Override
    public double OutOfSurf(double Data) {
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        if(data > pack_data){
            return 0;
        }
        if(data + Data > pack_data){
            if(data + Data > pack_data){
                return Double.parseDouble(formatDouble.format((pack_data - data)/1024));
            }
        }
        return -1;
    }


    public void text(int Message){
        message += Message;
        fee +=Message*0.2;
    }

    @Override
    public int OutOfText(int message) {
        return -1;
    }

    public void printPackageUse(SimCard s){
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        System.out.println(s.getTelephone()+"用户的"+PackageName+"使用情况:");
        System.out.println(PackageName+":");
        System.out.println("通话时长:"+phone_call+"分钟");
        String format_data = formatDouble.format(data/1024);
        System.out.println("上网流量:"+format_data+"MB");
        System.out.println("短信条数:"+message+"条");
        String format_fee = formatDouble.format(fee);
        System.out.println("本月消费:"+format_fee+"元");
    }

    @Override
    public void printPackageLeft(SimCard s) {

    }

    public void printPackageLeft(){
        System.out.println("您没有选择相应套餐，按照普通定价计费");

    }
    public void DeleteAllInfo(){
        PackageNumber = 4;
        phone_call = 0;
        data = 0;
        message = 0;
        fee = 0;
    }
    public void setDataPack(int i){
        pack_data += i * 1024;
    }
}
