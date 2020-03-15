import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class NetWorm extends MonthlyPackage implements Surf, DataPack {
    private static MySQL_PackageInfo packUtil = new MySQL_PackageInfo();

    public void setDataPack(int i){
        pack_data += i * 1024;
    }

    /*NetWorm(){
        PackageNumber = 2;
        phone_call = 0;
        data = 0;
        message = 0;
        fee = 68;
        PackageName = "网虫套餐";
    }*/


    NetWorm(){
        PackageName = "网虫套餐";
        PackageNumber = 2;
        try {
            packUtil.ModifyPackageInformation(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printPackage(){
        System.out.println("套餐类型:网虫套餐");
        System.out.println("通话时长:0分钟");
        System.out.println("上网流量:3GB");
        System.out.println("短信条数:0条");
        System.out.println("--------------------");
        System.out.println("月资费:68元");
        printOutPackage();
    }
    public void printToText(BufferedWriter bw) throws IOException {
        String[] packageInfo = {"套餐类型:网虫套餐\n","通话时长:0分钟\n","上网流量:3GB\n",
                "短信条数:0条\n","月资费:68元\n","——————————————————————————————————\n"
        };
        for(int i = 0; i < packageInfo.length; i++){
            bw.append(packageInfo[i]);
            bw.flush();
        }
    }
    public void call(double CallTime){
        System.out.println("该套餐不包含通话服务，龙龙将按照超出套餐包的价格收费。");
        phone_call += CallTime;
        fee += CallTime*0.2;
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
        System.out.println("该套餐不包含发送短信服务，龙龙将按照超出套餐包的价格收费。");
        message += Message;
        fee += Message*0.1;
    }
    public void printPackageUse(SimCard s){
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        System.out.println(s.getTelephone()+"用户的"+PackageName+"使用情况:");
        System.out.println("本套餐无通话服务，超出套餐的通话时长:"+phone_call+"分钟");
        String format_data = formatDouble.format(data/1024);
        System.out.println("上网流量:"+format_data+"GB");
        if(extra_data > 0){
            String format_extra_data = formatDouble.format(extra_data/1024);
            System.out.println("超出套餐的流量:"+format_extra_data+"GB");
        }
        System.out.println("本套餐无短信服务，超出套餐的短信条数:"+message+"条");
        String format_fee = formatDouble.format(fee-68);
        System.out.println("已提前扣费68元，本月额外消费:"+format_fee+"元");
        String total_fee = formatDouble.format(fee);
        System.out.println("合计:"+total_fee+"元");
    }
    public void printPackageLeft(SimCard s){
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        System.out.println(s.getTelephone()+"用户的"+PackageName+"剩余:");
        if(extra_data > 0){
            System.out.println("套餐的流量剩余:"+0+"MB");
        }
        else{
            String format_left_data = formatDouble.format((pack_data - data)/1024);
            System.out.println("套餐的流量剩余:"+format_left_data+"GB");
        }
    }
    public void DeleteAllInfo(){
        PackageNumber = 2;
        phone_call = 0;
        data = 0;
        message = 0;
        fee = 68;
    }

}
