import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Scanner;

public class ServiceHall implements InputAssure{
    static private MySQL_ServiceHall serviceUtil;


    static private MySQL_PackageInfo packUtil = new MySQL_PackageInfo();

    static private BaseDAO databaseUtil = new BaseDAO();
    static {
       /* try{
            databaseUtil.init();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            serviceUtil = new MySQL_ServiceHall();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            packUtil.PackageInformation(new File("资费说明.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private Customer customers;

  static {
        try {
            customers = new Customer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private Util utility = new Util();
    static public int getInput(Scanner scanner, String input, int up, int down) throws Exception{

        int count = 1, output = 0, i = 0;
        boolean int_number = false, valid_number = false;
        for(; i < input.length(); i++ ){
            if(input.charAt(i) < '0' || input.charAt(i) > '9'){
                break;
            }
        }
        if(i == input.length()){

            int_number = true;
            try{
                output = Integer.parseInt(input);
            }catch(Exception e)
            {
                System.out.println("输入有误，请输入数字"+down+"-"+up);
                count++;
            }
            if(output >= down && output <= up){
                valid_number = true;
            }
        }
        while (!int_number || !valid_number) {
            i = 0;
            System.out.println("输入有误，请输入数字"+down+"-"+up);
            input = scanner.next();
            count++;
            int_number = false;
            valid_number = false;
            for(; i < input.length(); i++ ){
                if(input.charAt(i) < '0' || input.charAt(i) > '9'){
                    break;
                }
            }
            if(i == input.length()){
                int_number = true;
                try{
                    output = Integer.parseInt(input);
                }catch(Exception e)
                {
                    System.out.println("输入有误，请输入数字"+down+"-"+up);
                    count++;
                }
                if(output >= down && output <= up){
                    valid_number = true;
                }
            }
            if(count > 10){
                break;
            }
            //throw new MyException("输入数据有误，请输入"+down+"-"+up);
        }
        if(count > 10) {
            System.out.println("多次输入错误，系统已自动退出");
            return -1;
        }
        return output;
    }
    static private int getInput(String input, int up, int down) throws Exception{
        return 0;
    }
    static private void RootMenu() throws Exception {
        Timer timer = new Timer();
        timer.schedule(customers,0,10*1000);
        Scanner scanner = new Scanner(System.in);
        UserScene.initializeUserScene();
        while(true){
            System.out.println("**********欢迎来到龙龙移动业务大厅**********");
            System.out.println("1、用户注册 2、用户登录 3、使用龙龙 4、退出系统");
            System.out.println("请输入您的选择: ");
            String first = scanner.next();
            int first_menu = 0;
            /*对输入进行正确性判断*/
            first_menu = getInput(scanner,first,4,1);
            if(first_menu == -1){
                customers.stop = true;
                timer.cancel();
                return;
            }
            /*根据输入进入不同的子菜单中*/
            if(first_menu == 1){
                int i = FirstMenu_1();
                if(i == 0){
                    continue;
                }
                if(i == -1){
                    customers.stop = true;
                    timer.cancel();
                    return;
                }
            }
            if(first_menu == 2){
                int i = FirstMenu_2();
                if(i == 0){
                    continue;
                }
                if(i == -1){
                    customers.stop = true;
                    timer.cancel();
                    return;
                }
            }

            if(first_menu == 3) {
                int i = FirstMenu_3();
                if(i == 0){
                    continue;
                }
                if(i == -1){
                    customers.stop = true;
                    timer.cancel();
                    return;
                }
            }

            if(first_menu == 4){
                System.out.println("谢谢，欢迎下次光临！");
                customers.stop = true;
                timer.cancel();
                break;
            }
        }
    }

    static private int FirstMenu_1() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********龙龙为您挑了9个电话号码，请您选择**********");
        /*随机生成9个电话号码并打印*/
        HashMap<Integer, String> randomTele = new HashMap<Integer, String>();
        String tele;
        Random r = new Random();
        for(int i = 0; i < 9; i++){
            tele = "189";
            for(int j = 0; j < 8; j++){
                int bit = r.nextInt(10);
                tele = tele + bit;
            }
            if(randomTele.containsValue(tele)){
                i--;
            }
            else{
                randomTele.put(i,tele);
            }
        }
        for(int i = 0; i < 9; i++){
            String random_telephone = randomTele.get(i);
            if((i + 1) % 3 == 0){
                System.out.print((i+1)+". "+random_telephone+'\n');
            }
            else{
                System.out.print((i+1)+". "+random_telephone+" ");
            }
        }
        String sign_in = scanner.next();
        int telephone = 0;
        telephone = getInput(scanner, sign_in, 9, 1);
        SimCard new_customer = new SimCard();
        new_customer.setTelephone(randomTele.get(telephone - 1));
        System.out.println("请输入您的用户名:");
        String name = scanner.next();
        new_customer.setUsername(name);
        System.out.println("请设置您的手机账户密码:");
        String new_password = scanner.next();
        System.out.println("请再输入一遍:");
        String new_password_a = scanner.next();
        int count = 0;//当输入密码前后多次不匹配则返回上一级
        while(!new_password.equals(new_password_a)){
            System.out.println("两次输入的密码不一致,请重新输入");
            new_password_a = scanner.next();
            count ++;
            if(count > 3){
                break;
            }
        }
        if(new_password.equals(new_password_a)){
            new_customer.setPassword(new_password);
            System.out.println("注册成功");
        }
        else if(count > 3){
            System.out.println("密码多次输入不匹配，请重新注册");
            return 0;
        }
        System.out.println("1、话痨套餐，2、网虫套餐，3、超人套餐。请选择套餐(输入序号):");
        String pack = scanner.next();
        int pack_choice  = 0;
        pack_choice = getInput(scanner, pack, 3, 1);
        pack_choice = (char) (pack_choice + 48);
        new_customer.setPackageNumber((char)pack_choice);
        System.out.println("请输入预存话费:");
        double money = scanner.nextDouble();

        if(money >= new_customer.getPackageNumber().fee){
            System.out.println("预存成功");

        }
        else{
            System.out.print("预存金额无法支付当前选择套餐的费用，请输入超过当前套餐包月");
            System.out.print(new_customer.getPackageNumber().fee);
            System.out.print("元的话费，请重新输入\n");
            money = scanner.nextDouble();
        }

        String dep = "充值"+money+"元";
        customers.add_customer(new_customer);
        customers.add_deposit(new_customer,dep);
        new_customer.DepositBalance(money);
        //serviceUtil.UpdateDeposit(new_customer, dep);
        new_customer.ConsumeBalance(new_customer.getPackageNumber().fee);
        System.out.println(new_customer.getBalance());
        new_customer.printForNew();

        return 0;

    }
    static private int FirstMenu_2() throws Exception{
        Scanner scanner = new Scanner(System.in);
        boolean flag = false;
        System.out.println("请输入您的手机号: ");
        String phone_number = scanner.next();
        boolean res = customers.getCustomer(phone_number);
        if(!res){
            System.out.println("您还没有龙龙移动的电话卡，请尽快注册哟！");
            return 0;
        }
        String in_password, re_password;
        System.out.println("请输入您的密码: (忘记密码请输入#)");
        //加一个找回密码的功能
        in_password = scanner.next();
        SimCard curr_customer = customers.getCustomerInfo(phone_number,in_password);
        if(curr_customer != null){
            System.out.println("输入密码成功！");
            while(true){
                System.out.println("**********龙龙很高兴能为您服务**********");
                System.out.println("1、业务办理 2、查询服务 3、办理退网 4、退出系统");
                int service = scanner.nextInt();

                try{
                    if(service > 4 || service < 1){
                        throw new MyException("输入超界");
                    }
                }catch(MyException e){
                    e.getMessage();
                    System.out.println("请输入数字1-4");
                    return 0;
                }
                switch (service){
                    case 1: {
                        int i =SecondMenu_1(curr_customer);
                        if(i == 1){
                            continue;
                        }
                        if(i == -1){
                            return -1;
                        }
                        //回退主界面
                        return 0;
                    }
                    case 2:{
                        int i = SecondMenu_2(curr_customer);
                        if(i == 1){
                            continue;
                        }
                        if(i == -1){
                            return -1;
                        }
                        //回退主界面
                        return 0;
                    }
                    case 3:{
                        System.out.println("**********龙龙很难过，你真的要选择退网吗(输入y/n)**********");
                        String back_off = scanner.next();
                        if(back_off.equals("y")){
                            customers.remove(phone_number);
                            System.out.println("感谢您的使用，希望未来您还能继续使用龙龙移动哦！");
                            customers.stop = true;
                            //退出系统
                            return -1;
                        }
                        else{
                            //回退主界面
                            return 0;
                        }
                    }
                    case 4:{
                        System.out.println("谢谢，欢迎下次光临！");
                        customers.stop = true;
                        //退出系统
                        return -1;
                    }
                }
            }

        }
        else if(in_password.equals("#")){
            System.out.println("**********密码重置**********");
            System.out.println("请确认您的电话号码:"+phone_number);
            System.out.println("确定(y/n)");
            String certify = scanner.next();
            if(certify.equals("y")){
                System.out.println("请输入验证码:");
                String verify_code = scanner.next();
                if(verify_code.equals("123456")){
                    System.out.println("请输入您的新密码:");
                    String new_password = scanner.next();
                    System.out.println("请再输入一遍:");
                    String new_password_a = scanner.next();
                    if(new_password.equals(new_password_a)){
                        curr_customer.setPassword(new_password);
                        flag = true;
                        System.out.println("密码修改成功");
                    }
                    else{
                        System.out.println("密码修改失败");
                        //退回主界面
                        return 0;
                    }
                }
                else{
                    System.out.println("验证码不正确");
                    //退回主界面
                    return 0;
                }
            }
            else{
                System.out.println("手机号码不匹配");
                //退回主界面
                return 0;
            }
            if(flag){
                System.out.println("请重新登录");
                return 0;//主界面
            }

        }
        else{
            System.out.println("密码输入不正确");
            return 0;
        }
        return 0;
    }
    static private int FirstMenu_3() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********欢迎使用龙龙移动**********");
        System.out.println("请输入手机号:");
        String phone_number = scanner.next();
        SimCard curr_customer = customers.getCustomerToUse(phone_number);

        if(curr_customer == null){
            System.out.println("号码不存在");
            //返回上一级
            return 0;
        }
        else {
            System.out.println("系统预设使用龙龙示例:");
            String[] action = UserScene.getMode();
            assert action != null; //断言
            String mode = action[1];
            int number = Integer.parseInt(action[2]);
            System.out.println(action[0]);
            if (mode.equals("通话")) {
                if (curr_customer.getPackageNumber() instanceof NetWorm) {
                    System.out.println("您的套餐不支持此项消费。");
                }
                else {
                    if (curr_customer.getPackageNumber() instanceof NormalPackage) {
                        String con = action[1] + " " + action[2] + "分钟";
                        customers.add_consumption(curr_customer, con);
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("通话",flow);
                        try {
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                curr_customer.ConsumeBalance(curr_fee);
                                packUtil.UpdatePackageInformation(curr_customer);
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            //返回主界面
                            return 0;
                        }
                    }
                    if (curr_customer.getPackageNumber() instanceof TalkingTooMuch) {
                        TalkingTooMuch p = (TalkingTooMuch) curr_customer.getPackageNumber();
                        String con = action[1] + " " + action[2] + "分钟";
                        customers.add_consumption(curr_customer, con);
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("通话",flow);
                        try {
                            if (p.OutOfCall(number) == 0) {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            } else if (p.OutOfCall(number) == -1) {

                            } else {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您已经通话"+p.OutOfCall(number)+"分钟，套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            }
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            //返回主界面
                            return 0;
                        }
                    }
                    if (curr_customer.getPackageNumber() instanceof Superman) {
                        Superman p = (Superman) curr_customer.getPackageNumber();
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("通话",flow);
                        String con = action[1] + " " + action[2] + "分钟";
                        customers.add_consumption(curr_customer, con);
                        try {
                            if (p.OutOfCall(number) == 0) {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            } else if (p.OutOfCall(number) == -1) {

                            } else {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您已经通话"+p.OutOfCall(number)+"分钟，套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            }
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            //返回主界面
                            return 0;
                        }
                    }
                }
            }

            if(mode.equals("短信")){
                if(curr_customer.getPackageNumber() instanceof NetWorm){
                    System.out.println("您的套餐不支持此项消费。");
                }
                else{
                    if(curr_customer.getPackageNumber() instanceof NormalPackage){
                        String con = action[1]+" "+action[2] + "条";
                        customers.add_consumption(curr_customer, con);
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("短信", flow);
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            //返回主界面
                            return 0;
                        }
                    }
                    if(curr_customer.getPackageNumber() instanceof TalkingTooMuch){
                        TalkingTooMuch p = (TalkingTooMuch) curr_customer.getPackageNumber();
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("短信", flow);
                        String con = action[1] + " " + action[2] + "条";
                        customers.add_consumption(curr_customer, con);
                        try{
                            if(p.OutOfText(number) == 0){
                                System.out.println("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                            else if(p.OutOfText(number) == -1){

                            }
                            else{
                                System.out.println("您已经完成\"+p.OutOfText(number)+\"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您已经完成"+p.OutOfText(number)+"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            //返回主界面
                            return 0;
                        }
                    }
                    if(curr_customer.getPackageNumber() instanceof Superman){
                        Superman p = (Superman) curr_customer.getPackageNumber();
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("短信", flow);
                        String con = action[1]+" "+action[2] + "条";
                        customers.add_consumption(curr_customer, con);
                        try{
                            if(p.OutOfText(number) == 0){
                                System.out.println("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                            else if(p.OutOfText(number) == -1){

                            }
                            else{
                                System.out.println("您已经完成"+p.OutOfText(number)+"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您已经完成"+p.OutOfText(number)+"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                    }
                }
            }
            if(mode.equals("流量")){
                if(curr_customer.getPackageNumber() instanceof TalkingTooMuch){
                    System.out.println("您的套餐不支持此项消费。");
                }
                else{
                    if(curr_customer.getPackageNumber() instanceof NormalPackage){
                        NormalPackage p = (NormalPackage) curr_customer.getPackageNumber();
                        String con = action[1]+" "+action[2] + "GB";
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("流量", flow*1024);
                        customers.add_consumption(curr_customer, con);
                        try{
                            if(p.OutOfSurf(number) == 0){
                                System.out.println("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                                throw new MyException("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                            }
                            else if(p.OutOfSurf(number) == -1){

                            }
                            else{
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                String used_data = decimalFormat.format(p.OutOfSurf(number));
                                System.out.println("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                                throw new MyException("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                    }
                    if(curr_customer.getPackageNumber() instanceof NetWorm){
                        NetWorm p = (NetWorm) curr_customer.getPackageNumber();
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("流量", flow*1024);
                        String con = action[1] + " " + action[2] + "GB";
                        customers.add_consumption(curr_customer, con);
                        try{
                            if(p.OutOfSurf(number) == 0){
                                System.out.println("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                                throw new MyException("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                            }
                            else if(p.OutOfSurf(number) == -1){

                            }
                            else{
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                String used_data = decimalFormat.format(p.OutOfSurf(number));
                                System.out.println("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                                throw new MyException("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                    }
                    if(curr_customer.getPackageNumber() instanceof Superman){
                        Superman p = (Superman) curr_customer.getPackageNumber();
                        String con = action[1]+" "+action[2] + "GB";
                        customers.add_consumption(curr_customer, con);
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("流量", flow*1024);
                        try{
                            if(p.OutOfSurf(number) == 0){
                                System.out.println("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                                throw new MyException("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                            }
                            else if(p.OutOfSurf(number) == -1){

                            }
                            else{
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                String used_data = decimalFormat.format(p.OutOfSurf(number));
                                System.out.println("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                                throw new MyException("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                    }
                }
            }
            System.out.println("用户自定义模式:");
            MonthlyPackage m = curr_customer.getPackageNumber();
            double curr_fee = 0;
            if(m instanceof TalkingTooMuch){
                TalkingTooMuch p = (TalkingTooMuch) m;
                System.out.println("1、通话 2、发短信");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:{
                        System.out.println("请拨号:");
                        String call_phoneNo = scanner.next();
                        System.out.println("请输入通话时间:");
                        int time = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("通话",time);
                        System.out.println("用户"+curr_customer.getTelephone()+"给用户"+call_phoneNo+"拨打了"+time+"分钟电话");
                        String con = "通话 " + time + "分钟";
                        customers.add_consumption(curr_customer,con);
                        try {
                            if (p.OutOfCall(number) == 0) {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            } else if (p.OutOfCall(number) == -1) {

                            } else {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您已经通话"+p.OutOfCall(number)+"分钟，套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            }
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }

                        break;
                    }
                    case 2:{
                        System.out.println("请拨号:");
                        String call_phoneNo = scanner.next();
                        System.out.println("请输入发送短信条数");
                        int message = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("短信",message);
                        System.out.println("用户"+curr_customer.getTelephone()+"给用户"+call_phoneNo+"发送了"+message+"条短信");
                        String con = "短信 " + message + "条";
                        customers.add_consumption(curr_customer,con);
                        try{
                            if(p.OutOfText(number) == 0){
                                System.out.println("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                            else if(p.OutOfText(number) == -1){

                            }
                            else{
                                System.out.println("您已经完成"+p.OutOfText(number)+"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您已经完成"+p.OutOfText(number)+"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                        break;
                    }
                }
            }
            if(m instanceof NetWorm){
                NetWorm p = (NetWorm) m;
                System.out.println("请输入您的上网耗费流量");
                int data = scanner.nextInt();
                curr_fee = curr_customer.UseTelephone("流量",data);
                System.out.println("用户"+curr_customer.getTelephone()+"上网使用了了"+data+"MB流量");
                String con = "流量 " + data + "MB";
                customers.add_consumption(curr_customer,con);
                try{
                    if(p.OutOfSurf(number) == 0){
                        System.out.println("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                        throw new MyException("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                    }
                    else if(p.OutOfSurf(number) == -1){

                    }
                    else{
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        String used_data = decimalFormat.format(p.OutOfSurf(number));
                        System.out.println("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                        throw new MyException("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                    }
                } catch(MyException e) {
                    e.printStackTrace();
                }
                try{
                    if(!utility.BalanceEnough(curr_customer,curr_fee)){
                        System.out.println("您的余额不足，请尽快充值！");
                        throw new MyException("您的余额不足，请尽快充值！");
                    }
                    curr_customer.ConsumeBalance(curr_fee);
                    packUtil.UpdatePackageInformation(curr_customer);
                } catch(MyException e){
                    e.printStackTrace();
                    curr_customer.ConsumeBalance(curr_fee);
                    packUtil.UpdatePackageInformation(curr_customer);
                    return 0;
                }
            }
            if(m instanceof Superman){
                Superman p = (Superman) m;
                System.out.println("1、通话 2、发短信 3、上网");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:{
                        System.out.println("请拨号:");
                        String call_phoneNo = scanner.next();
                        System.out.println("请输入通话时间:");
                        int time = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("通话",time);
                        System.out.println("用户"+curr_customer.getTelephone()+"给用户"+call_phoneNo+"拨打了"+time+"分钟电话");
                        String con = "通话 " + time + "分钟";
                        customers.add_consumption(curr_customer,con);
                        try {
                            if (p.OutOfCall(number) == 0) {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            } else if (p.OutOfCall(number) == -1) {

                            } else {
                                System.out.println("您套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                                throw new MyException("您已经通话"+p.OutOfCall(number)+"分钟，套餐里的通话时长已被用完，正在按超额通话价格扣费！");
                            }
                        } catch (MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                        break;
                    }
                    case 2:{
                        System.out.println("请拨号:");
                        String call_phoneNo = scanner.next();
                        System.out.println("请输入发送短信条数");
                        int message = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("短信",message);
                        System.out.println("用户"+curr_customer.getTelephone()+"给用户"+call_phoneNo+"发送了"+message+"条短信");
                        String con = "短信 " + message + "条";
                        customers.add_consumption(curr_customer,con);
                        packUtil.UpdatePackageInformation(curr_customer);
                        try{
                            if(p.OutOfText(number) == 0){
                                System.out.println("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                            else if(p.OutOfText(number) == -1){

                            }
                            else{
                                System.out.println("您已经完成"+p.OutOfText(number)+"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                                throw new MyException("您已经完成"+p.OutOfText(number)+"条短信的发送，套餐里的短信条数已被用完，正在按超额发短信价格扣费！");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        }
                        break;
                    }
                    case 3:{
                        System.out.println("请输入您的上网耗费流量");
                        int data = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("流量",data);
                        System.out.println("用户"+curr_customer.getTelephone()+"上网使用了了"+data+"MB流量");
                        String con = "流量 " + data + "MB";
                        customers.add_consumption(curr_customer,con);
                        try{
                            if(p.OutOfSurf(number) == 0){
                                System.out.println("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                                throw new MyException("您套餐里的流量已被用完，正在按超额流量价格扣费！");
                            }
                            else if(p.OutOfSurf(number) == -1){

                            }
                            else{
                                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                String used_data = decimalFormat.format(p.OutOfSurf(number));
                                System.out.println("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                                throw new MyException("您已使用了"+used_data+"GB流量，超过套餐使用限额，正在按超额流量价格扣费");
                            }
                        } catch(MyException e) {
                            e.printStackTrace();
                        }
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                        break;
                    }
                }
            }
            if(m instanceof NormalPackage){
                System.out.println("1、通话 2、发短信 3、上网");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:{
                        System.out.println("请拨号:");
                        String call_phoneNo = scanner.next();
                        System.out.println("请输入通话时间:");
                        int time = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("通话",time);
                        System.out.println("用户"+curr_customer.getTelephone()+"给用户"+call_phoneNo+"拨打了"+time+"分钟电话");
                        String con = "通话 " + time + "分钟";
                        customers.add_consumption(curr_customer,con);
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                        break;
                    }
                    case 2:{
                        System.out.println("请拨号:");
                        String call_phoneNo = scanner.next();
                        System.out.println("请输入发送短信条数");
                        int message = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("短信",message);
                        System.out.println("用户"+curr_customer.getTelephone()+"给用户"+call_phoneNo+"发送了"+message+"条短信");
                        String con = "短信 " + message + "条";
                        customers.add_consumption(curr_customer,con);
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                        break;
                    }
                    case 3:{
                        System.out.println("请输入您的上网耗费流量");
                        int data = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("流量",data);
                        System.out.println("用户"+curr_customer.getTelephone()+"上网使用了了"+data+"MB流量");
                        String con = "流量 " + data + "MB";
                        customers.add_consumption(curr_customer,con);
                        try{
                            if(!utility.BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                        } catch(MyException e){
                            e.printStackTrace();
                            curr_customer.ConsumeBalance(curr_fee);
                            packUtil.UpdatePackageInformation(curr_customer);
                            return 0;
                        }
                        break;
                    }
                }
            }
        }
        return 0;
    }

    static private int SecondMenu_1(SimCard curr_customer) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("**********龙龙很高兴能为您办理业务**********");
            System.out.println("1、主套餐 2、流量包 3、生活服务 4、话费充值 ");
            int business = scanner.nextInt();
            try{
                if(business > 4 || business < 1){
                    System.out.println("输出超界");
                    throw new MyException("输入超界");
                }
            }catch(MyException e){
                e.getMessage();
                //返回第一级菜单
                return 1;
            }

            switch (business) {
                case 1: {
                    int i = ThirdMenu_1(curr_customer);
                    if(i == 2){
                        continue;
                    }
                    if(i == 1){
                        return 1;
                    }
                    break;
                }
                case 2: {
                    int i = ThirdMenu_2(curr_customer);
                    if(i == 2){
                        continue;
                    }
                    if(i == 1){
                        return 1;
                    }
                    break;
                }
                case 3: {
                    int i = ThirdMenu_3(curr_customer);
                    if(i == 1){
                        return 1;
                    }
                    break;
                }
                case 4: {
                    int i = ThirdMenu_4(curr_customer);
                    if(i==2){
                        continue;
                    }
                    if(i == -1){
                        return -1;
                    }
                    break;
                }
            }
        }
    }
    static private int SecondMenu_2(SimCard curr_customer) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********龙龙很高兴能帮您查询**********");
        System.out.println("1、本月账单打印 2、套餐余量查询 3、缴费记录查询 4、消费记录查询 5、资费说明");
        int information = scanner.nextInt();
        try{
            if(information < 1 || information > 5){
                System.out.println("输出超界");
                throw new MyException("输出超界，返回上一级");
            }
        }catch (MyException e){
            e.getMessage();
            return 1;//返回一级菜单
        }
        switch(information){
            case 1:{
                Util.printBill(curr_customer);
                break;
            }
            case 2:{
                Util.printLeft(curr_customer);
                break;
            }
            case 3:{
                customers.print_deposit(curr_customer);
                break;
            }
            case 4:{
                customers.print_consumption(curr_customer);
                break;
            }
            case 5:{
                Util.PrintPackageInfo();
                break;
            }
        }
        //返回一级菜单
        return 1;
    }

    static private int ThirdMenu_1(SimCard curr_customer) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********龙龙移动有以下几种套餐供您选择**********");
        System.out.println("1、话痨套餐 2、网虫套餐 3、超人套餐 4、普通计费 ");
        int Package = scanner.nextInt();
        try{
            if(Package < 1 || Package > 4){
                throw new MyException("输出超界，返回上一级");
            }
        }catch (MyException e){
            e.getMessage();
            return 2;
        }
        MonthlyPackage curr_package = curr_customer.getPackageNumber();
        switch (Package) {
            case 1: {
                TalkingTooMuch talking = new TalkingTooMuch();
                talking.printPackage();
                if (curr_package.PackageNumber == '0') {
                    System.out.println("您还没有选择套餐，您需要选择这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,talking)){
                            //curr_customer.setMonthly_package('1');
                            Util.ConsumeMonthlyPackage(curr_customer,'1');
                            packUtil.ChangePackage(curr_customer,"话痨套餐");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                } else if (curr_package.PackageNumber == 1) {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您正在查看您自己的套餐明细。");
                    return 1;
                } else {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您需要更换成这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,talking)){
                            //curr_customer.setMonthly_package('1');
                            Util.ConsumeMonthlyPackage(curr_customer,'1');
                            packUtil.ChangePackage(curr_customer,"话痨套餐");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                }
                break;
            }
            case 2: {
                NetWorm net = new NetWorm();
                net.printPackage();
                if (curr_package.PackageNumber == '0') {
                    System.out.println("您还没有选择套餐，您需要选择这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,net)){
                            //curr_customer.setMonthly_package('2');
                            Util.ConsumeMonthlyPackage(curr_customer,'2');
                            packUtil.ChangePackage(curr_customer,"网虫套餐");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                } else if (curr_package.PackageNumber == 2) {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您正在查看您自己的套餐明细。");
                    return 1;

                } else {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您需要更换成这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,net)){
                            //curr_customer.setMonthly_package('2');
                            Util.ConsumeMonthlyPackage(curr_customer,'2');
                            packUtil.ChangePackage(curr_customer,"网虫套餐");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                }
                break;
            }
            case 3: {
                Superman superman = new Superman();
                superman.printPackage();
                if (curr_package.PackageNumber == '0') {
                    System.out.println("您还没有选择套餐，您需要选择这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,superman)){
                            //curr_customer.setMonthly_package('3');
                            Util.ConsumeMonthlyPackage(curr_customer,'3');
                            packUtil.ChangePackage(curr_customer,"超人套餐");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                } else if (curr_package.PackageNumber == 3) {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您正在查看您自己的套餐明细。");
                    return 1;

                } else {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您需要更换成这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,superman)){
                            //curr_customer.setMonthly_package('3');
                            Util.ConsumeMonthlyPackage(curr_customer,'3');
                            packUtil.ChangePackage(curr_customer,"超人套餐");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                }
                break;
            }
            case 4: {
                NormalPackage normal = new NormalPackage();
                normal.printPackage();
                if (curr_package.PackageNumber == '0') {
                    System.out.println("您还没有选择套餐，您需要选择这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,normal)){
                            //curr_customer.setMonthly_package('4');
                            Util.ConsumeMonthlyPackage(curr_customer,'4');
                            packUtil.ChangePackage(curr_customer,"普通计费");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                } else if (curr_package.PackageNumber == 4) {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您正在查看您自己的套餐明细。");
                    return 1;
                } else {
                    System.out.print("您现在的套餐是:" + curr_package.PackageName);
                    System.out.println(",您需要更换成这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(Util.BalanceEnough(curr_customer,normal)){
                            //curr_customer.setMonthly_package('4');
                            Util.ConsumeMonthlyPackage(curr_customer,'4');
                            packUtil.ChangePackage(curr_customer,"普通计费");
                            return 1;
                        }
                        else{
                            System.out.println("您的余额不足，请先充值。");
                            return 2;
                        }
                    }
                }
                break;
            }
        }
        return 0;
    }
    static private int ThirdMenu_2(SimCard curr_customer) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********龙龙超大流量包等你来拿**********");
        System.out.println("1、1GB 2、2GB 3、3GB ");
        int datapack = scanner.nextInt();
        try{
            if(datapack < 1 || datapack > 3){
                throw new MyException("输出超界，返回上一级");
            }
        }catch (MyException e){
            e.getMessage();
            return 2;
        }
        if(curr_customer.getPackageNumber() instanceof NetWorm){
            ((NetWorm) curr_customer.getPackageNumber()).setDataPack(datapack);
            if(utility.BalanceEnough(curr_customer,datapack*10)){
                curr_customer.ConsumeBalance(datapack*10);
            }
            else{
                System.out.println("您的余额不足，请先充值。");
                return 2;
            }
        }
        else if(curr_customer.getPackageNumber() instanceof Superman){
            ((Superman) curr_customer.getPackageNumber()).setDataPack(datapack);
            if(utility.BalanceEnough(curr_customer,datapack*10)){
                curr_customer.ConsumeBalance(datapack*10);
            }
            else{
                System.out.println("您的余额不足，请先充值。");
                return 2;
            }
        }
        else if(curr_customer.getPackageNumber() instanceof NormalPackage){
            ((NormalPackage) curr_customer.getPackageNumber()).setDataPack(datapack);
            if(utility.BalanceEnough(curr_customer,datapack*10)){
                curr_customer.ConsumeBalance(datapack*10);
            }
            else{
                System.out.println("您的余额不足，请先充值。");
                return 2;
            }
        }
        else{
            System.out.println("您的套餐不支持超大流量包");
            return 1;
        }
        return 1;
    }
    static private int ThirdMenu_3(SimCard curr_customer) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********龙龙为您的生活增光添彩**********");
        System.out.println("1、视频VIP 2、音乐VIP");
        int entertaining = scanner.nextInt();
        try {
            if (entertaining < 1 || entertaining > 3) {
                throw new MyException("输出超界，返回上一级");
            }
        } catch (MyException e) {
            e.getMessage();
            return 1;
        }
        curr_customer.setLife_service(entertaining);
        return 1;
    }
    static private int ThirdMenu_4(SimCard curr_customer) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********话费不足，记得要及时充值噢，请输入充值金额**********");
        double money = scanner.nextDouble();
        int count = 1;
        while (money < 50) {
            System.out.println("至少充值50元");
            money = scanner.nextDouble();
            count++;
            if (count > 10) {
                break;
            }
        }
        if (count > 10) {
            System.out.println("多次输入错误，系统自动退出");
            //退出系统
            return -1;
        } else {
            curr_customer.DepositBalance(money);
            String deposit_record = curr_customer.getTelephone() + " 充值 " + money + "元";
            customers.add_deposit(curr_customer, deposit_record);
            System.out.println("充值成功！");
            //返回二级菜单
            return 2;
        }
    }

    static public void main(String[] args) throws Exception {

        RootMenu();
        databaseUtil.closeAll();
        //customers.print_all_customers();
    }
}
