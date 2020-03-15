import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;

class Util {
    //打印本月账单
    static void printBill(SimCard s){
        DecimalFormat formatDouble = new DecimalFormat("0.00");
        MonthlyPackage p = s.getPackageNumber();
        if(p instanceof TalkingTooMuch){
            TalkingTooMuch talkingTooMuch = (TalkingTooMuch) p;
            talkingTooMuch.printPackageUse(s);
        }
        if(p instanceof NetWorm){
            NetWorm netWorm = (NetWorm) p;
            netWorm.printPackageUse(s);
        }
        if(p instanceof Superman){
            Superman superman = (Superman) p;
            superman.printPackageUse(s);
        }
        if(p instanceof NormalPackage){
            NormalPackage normalPackage = (NormalPackage) p;
            normalPackage.printPackageUse(s);
        }
        if(s.getBalance() >= 0){
            String format_left = formatDouble.format(s.getBalance());
            System.out.println("本月余额:"+format_left+"元");
        }
        else{
            System.out.println("本月余额:"+0+"元");
            String format_left = formatDouble.format(s.getBalance());
            System.out.println("本月欠费:"+format_left+"元");
        }

    }
    //打印套餐余量
    static void printLeft(SimCard s){
        MonthlyPackage p = s.getPackageNumber();
        if(p instanceof TalkingTooMuch){
            TalkingTooMuch talkingTooMuch = (TalkingTooMuch) p;
            talkingTooMuch.printPackageLeft(s);
        }
        if(p instanceof NetWorm){
            NetWorm netWorm = (NetWorm) p;
            netWorm.printPackageLeft(s);
        }
        if(p instanceof Superman){
            Superman superman = (Superman) p;
            superman.printPackageLeft(s);
        }
        if(p instanceof NormalPackage){
            NormalPackage normalPackage = (NormalPackage) p;
            normalPackage.printPackageLeft();
        }

    }
    //判断用户的余额是否充足
    static boolean BalanceEnough(SimCard s, MonthlyPackage p){
        double money = s.getBalance();
        if(p instanceof TalkingTooMuch){
            TalkingTooMuch talkingTooMuch = (TalkingTooMuch) p;
            if(money > talkingTooMuch.fee){
                return true;
            }
        }
        if(p instanceof NetWorm){
            NetWorm netWorm = (NetWorm) p;
            if(money > netWorm.fee){
                return true;
            }
        }
        if(p instanceof Superman){
            Superman superman = (Superman) p;
            if(money > superman.fee){
                return true;
            }
        }
        if(p instanceof NormalPackage){
            NormalPackage normal = (NormalPackage) p;
            return money > normal.fee;
        }
        return false;
    }
    static boolean BalanceEnough(SimCard s, double num){
        return s.getBalance() >= num;
    }
    //用户每月套餐的月租消费
    static void ConsumeMonthlyPackage(SimCard s, char new_package) throws Exception {
        MonthlyPackage p = s.getPackageNumber();
        if(p instanceof TalkingTooMuch){
            TalkingTooMuch talkingTooMuch = (TalkingTooMuch) p;
            talkingTooMuch.DeleteAllInfo();
            s.ConsumeBalance(talkingTooMuch.fee);
        }
        if(p instanceof NetWorm){
            NetWorm netWorm = (NetWorm) p;
            netWorm.DeleteAllInfo();
            s.ConsumeBalance(netWorm.fee);
        }
        if(p instanceof Superman){
            Superman superman = (Superman) p;
            superman.DeleteAllInfo();
            s.ConsumeBalance(superman.fee);
        }
        if(p instanceof NormalPackage){
            NormalPackage normalPackage = (NormalPackage) p;
            normalPackage.DeleteAllInfo();
            s.ConsumeBalance(normalPackage.fee);
        }
        s.setPackageNumber(new_package);
        System.out.println("更换套餐成功！");
    }
    //资费说明
    static void PrintPackageInfo()throws Exception{

        FileReader fr = new FileReader("资费说明.txt");
        BufferedReader br = new BufferedReader(fr);
        try{

            String str;
            while((str = br.readLine()) != null)
            {
                System.out.println(str);
            }

        }catch(Exception e){
            System.out.println("文件不存在");
            e.printStackTrace();
        }
        br.close();
        fr.close();
    }
    static void PrintOutPackageInfo()throws Exception{
        FileWriter fw = new FileWriter("资费说明.txt");
        FileReader fr = new FileReader("资费说明.txt");
        try{
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("----------资费说明----------\n");
            TalkingTooMuch t = new TalkingTooMuch();
            NetWorm n = new NetWorm();
            Superman s = new Superman();
            NormalPackage np = new NormalPackage();
            t.printToText(bw);
            n.printToText(bw);
            s.printToText(bw);
            t.printOutToText(bw);
            np.printToText(bw);

        }catch(Exception e){
            System.out.println("文件不存在");
            e.printStackTrace();
        }
        fw.close();
    }
}
