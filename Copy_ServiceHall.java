import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


/*public class Copy_ServiceHall implements InputAssure {
    static private Customer customers = new Customer();
    static private UserScene userScene = new UserScene();
    static public int RootMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        userScene.initializeUserScene();
        while(true){
            System.out.println("**********欢迎来到龙龙移动业务大厅**********");
            System.out.println("1、用户注册 2、用户登录 3、使用龙龙 4、退出系统");
            System.out.println("请输入您的选择: ");
            int first_menu = scanner.nextInt();
            first_menu = getInput(scanner,first_menu,4,1);
            if(first_menu == -1){
                return 0;
            }

            if(first_menu == 1){
                int i = FirstMenu_1();
                if(i == 0){
                    continue;
                }
                if(i == -1){
                    return 0;
                }
            }

            if(first_menu == 2){
                int i = FirstMenu_2();
                if(i == 0){
                    continue;
                }
                if(i == -1){
                    return 0;
                }
            }

            if(first_menu == 3) {
                int i = FirstMenu_3();
                if(i == 0){
                    continue;
                }
                if(i == -1){
                    return 0;
                }
            }

            if(first_menu == 4){
                System.out.println("谢谢，欢迎下次光临！");
                break;
            }
        }
        return 0;
    }
    static public int FirstMenu_1() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********龙龙为您挑了9个电话号码，请您选择**********");
        HashMap<Integer, String> randomTele = new HashMap<Integer, String>();
        String tele;
        Random r = new Random();
        for(int i = 0; i < 9; i++){
            tele = "189";
            for(int j = 0; j < 8; j++){
                int bit = r.nextInt(10);
                tele += Integer.toString(bit);
            }
            if(randomTele.containsValue(tele)){
                i--;
                continue;
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
        int telephone = Integer.parseInt(scanner.next());
        telephone = getInput(scanner, telephone, 9, 1);
        SimCard new_customer = new SimCard();
        new_customer.setTelephone(randomTele.get(telephone));
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
            System.out.println("密码设置成功");
        }
        else if(count > 3){
            System.out.println("密码多次输入不匹配，请重新注册");
            return 0;
        }
        System.out.println("1、话痨套餐，2、网虫套餐，3、超人套餐。请选择套餐(输入序号):");
        int pack_choice  = scanner.nextInt();
        pack_choice = getInput(scanner, pack_choice, 3, 1);
        pack_choice = (char) (pack_choice + 48);
        new_customer.setMonthly_package((char)pack_choice);
        System.out.println("请输入预存话费:");
        double money;
        try{
            money = scanner.nextDouble();
            if (money <= 0){
                throw new MyException("预存金额必须大于零");
            }
            if(!BalanceEnough(new_customer,new_customer.getMonthly_package())){
                throw new MyException("预存金额无法支付当前选择套餐的费用，请输入超过当前套餐包月"
                        +new_customer.getMonthly_package().fee+"元的话费");
            }
        }catch(MyException e){
            money = scanner.nextDouble();
        }
        customers.add_customer(new_customer);
        new_customer.DepositBalance(money);
        new_customer.ConsumeBalance(new_customer.getMonthly_package().fee);
        new_customer.printForNew();
        return 0;
    }
    static public int FirstMenu_2() throws Exception{
        Scanner scanner = new Scanner(System.in);
        boolean flag = false;
        System.out.println("请输入您的手机号: ");
        String phone_number = scanner.next();
        String in_password, re_password;
        System.out.println("请输入您的密码: (忘记密码请输入#)");
        //加一个找回密码的功能
        in_password = scanner.next();
        SimCard curr_customer = customers.getCustomer(phone_number);
        if(curr_customer == null){
            System.out.println("您还没有龙龙移动的电话卡，请尽快注册哟！");
            return 0;
        }
        if(in_password.equals(curr_customer.getPassword())){
            System.out.println("输入密码成功！");
            while(true){
                System.out.println("**********龙龙很高兴能为您服务**********");
                System.out.println("1、业务办理 2、查询服务 3、退网服务 4、退出系统");
                int service = scanner.nextInt();
                try{
                    if(service > 4 || service < 1){
                        throw new MyException("输入超界");
                    }
                }catch(MyException e){
                    e.getMessage();
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
        return 0;
    }
    static public int FirstMenu_3(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********欢迎使用龙龙移动**********");
        System.out.println("请输入手机号:");
        String phone_number = scanner.next();
        SimCard curr_customer = customers.getCustomer(phone_number);

        if(curr_customer == null){
            System.out.println("号码不存在");
            //返回上一级
            return 0;
        }
        else {
            System.out.println("系统预设使用龙龙示例:");
            String[] action = userScene.getMode();
            assert action != null; //断言
            String mode = action[1];
            int number = Integer.parseInt(action[2]);
            System.out.println(action[0]);
            if (mode.equals("通话")) {
                if (curr_customer.getMonthly_package() instanceof NetWorm) {
                    System.out.println("您的套餐不支持此项消费。");
                }
                else {
                    if (curr_customer.getMonthly_package() instanceof NormalPackage) {
                        String con = action[1] + " " + action[2];
                        customers.add_consumption(curr_customer, con);
                        double curr_fee = curr_customer.getMonthly_package().fee;
                        try{
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            //返回主界面
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                    if (curr_customer.getMonthly_package() instanceof TalkingTooMuch) {
                        TalkingTooMuch p = (TalkingTooMuch) curr_customer.getMonthly_package();
                        double curr_fee = p.fee;
                        int flow = Integer.parseInt(action[2]);
                        curr_customer.UseTelephone("通话", flow);
                        String con = action[1] + " " + action[2];
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
                            return 0;
                        }
                        try{
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            //返回主界面
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                    if (curr_customer.getMonthly_package() instanceof Superman) {
                        Superman p = (Superman) curr_customer.getMonthly_package();
                        double curr_fee = p.fee;
                        int flow = Integer.parseInt(action[2]);
                        curr_customer.UseTelephone("通话", flow);
                        String con = action[1] + " " + action[2];
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            //返回主界面
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                }
            }

            if(mode.equals("短信")){
                if(curr_customer.getMonthly_package() instanceof NetWorm){
                    System.out.println("您的套餐不支持此项消费。");
                }
                else{
                    if(curr_customer.getMonthly_package() instanceof NormalPackage){
                        String con = action[1]+" "+action[2];
                        customers.add_consumption(curr_customer, con);
                        int flow = Integer.parseInt(action[2]);
                        curr_customer.UseTelephone("短信", flow);
                        double curr_fee = curr_customer.getMonthly_package().fee;
                        try{
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            //返回主界面
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                    if(curr_customer.getMonthly_package() instanceof TalkingTooMuch){
                        TalkingTooMuch p = (TalkingTooMuch) curr_customer.getMonthly_package();
                        int flow = Integer.parseInt(action[2]);
                        curr_customer.UseTelephone("短信", flow);
                        double curr_fee = curr_customer.getMonthly_package().fee;
                        String con = action[1]+" "+action[2];
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            //返回主界面
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                    if(curr_customer.getMonthly_package() instanceof Superman){
                        Superman p = (Superman) curr_customer.getMonthly_package();
                        int flow = Integer.parseInt(action[2]);
                        curr_customer.UseTelephone("短信", flow);
                        double curr_fee = curr_customer.getMonthly_package().fee;
                        String con = action[1]+" "+action[2];
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                }
            }
            if(mode.equals("流量")){
                if(curr_customer.getMonthly_package() instanceof TalkingTooMuch){
                    System.out.println("您的套餐不支持此项消费。");
                }
                else{
                    if(curr_customer.getMonthly_package() instanceof NormalPackage){
                        String con = action[1]+" "+action[2];
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("流量", flow);
                        customers.add_consumption(curr_customer, con);
                        try{
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                    if(curr_customer.getMonthly_package() instanceof NetWorm){
                        NetWorm p = (NetWorm) curr_customer.getMonthly_package();
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("流量", flow);
                        String con = action[1] + " " + action[2];
                        customers.add_consumption(curr_customer, con);
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                    if(curr_customer.getMonthly_package() instanceof Superman){
                        Superman p = (Superman) curr_customer.getMonthly_package();
                        String con = action[1]+" "+action[2];
                        customers.add_consumption(curr_customer, con);
                        int flow = Integer.parseInt(action[2]);
                        double curr_fee = curr_customer.UseTelephone("流量", flow);
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        curr_customer.ConsumeBalance(curr_fee);
                    }
                }
            }
            System.out.println("用户自定义模式:");
            MonthlyPackage m = curr_customer.getMonthly_package();
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        break;
                    }
                }
                curr_customer.ConsumeBalance(curr_fee);
            }
            if(m instanceof NetWorm){
                NetWorm p = (NetWorm) m;
                System.out.println("请输入您的上网耗费流量");
                int data = scanner.nextInt();
                curr_fee = curr_customer.UseTelephone("上网",data);
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
                    if(!BalanceEnough(curr_customer,curr_fee)){
                        System.out.println("您的余额不足，请尽快充值！");
                        curr_customer.ConsumeBalance(curr_fee);
                        throw new MyException("您的余额不足，请尽快充值！");
                    }
                } catch(MyException e){
                    e.printStackTrace();
                    return 0;
                }
                curr_customer.ConsumeBalance(curr_fee);
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 3:{
                        System.out.println("请输入您的上网耗费流量");
                        int data = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("上网",data);
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        break;
                    }
                }
                curr_customer.ConsumeBalance(curr_fee);
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
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
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        break;
                    }
                    case 3:{
                        System.out.println("请输入您的上网耗费流量");
                        int data = scanner.nextInt();
                        curr_fee = curr_customer.UseTelephone("上网",data);
                        System.out.println("用户"+curr_customer.getTelephone()+"上网使用了了"+data+"MB流量");
                        String con = "流量 " + data + "MB";
                        customers.add_consumption(curr_customer,con);
                        try{
                            if(!BalanceEnough(curr_customer,curr_fee)){
                                System.out.println("您的余额不足，请尽快充值！");
                                curr_customer.ConsumeBalance(curr_fee);
                                throw new MyException("您的余额不足，请尽快充值！");
                            }
                        } catch(MyException e){
                            e.printStackTrace();
                            return 0;
                        }
                        break;
                    }
                }
                curr_customer.ConsumeBalance(curr_fee);
            }
        }
        return 0;
    }

    static public int SecondMenu_1(SimCard curr_customer){
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
    static public int SecondMenu_2(SimCard curr_customer) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("**********龙龙很高兴能帮您查询**********");
        System.out.println("1、本月账单打印 2、套餐余量查询 3、缴费记录查询 4、消费记录查询 5、资费说明");
        int information = scanner.nextInt();
        try{
            if(information < 1 || information > 3){
                System.out.println("输出超界");
                throw new MyException("输出超界，返回上一级");
            }
        }catch (MyException e){
            e.getMessage();
            //返回一级菜单
            return 1;
        }
        switch(information){
            case 1:{
                printBill(curr_customer);
                break;
            }
            case 2:{
                printLeft(curr_customer);
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
                FileWriter fw = new FileWriter("资费说明.txt");
                try{
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("----------资费说明----------");
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
                }
                fw.close();
                break;
            }
        }
        //返回一级菜单
        return 1;
    }
    static public int ThirdMenu_1(SimCard curr_customer){
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
        MonthlyPackage curr_package = curr_customer.getMonthly_package();
        switch (Package) {
            case 1: {
                TalkingTooMuch talking = new TalkingTooMuch();
                talking.printPackage();
                if (curr_package.PackageNumber == '0') {
                    System.out.println("您还没有选择套餐，您需要选择这个套餐吗？(y/n)");
                    String choice = scanner.next();
                    if (choice.equals("y")) {
                        if(BalanceEnough(curr_customer,talking)){
                            curr_customer.setMonthly_package('1');
                            ConsumeMonthlyPackage(curr_customer);
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
                        if(BalanceEnough(curr_customer,talking)){
                            curr_customer.setMonthly_package('1');
                            ConsumeMonthlyPackage(curr_customer);
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
                        if(BalanceEnough(curr_customer,net)){
                            curr_customer.setMonthly_package('2');
                            ConsumeMonthlyPackage(curr_customer);
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
                        if(BalanceEnough(curr_customer,net)){
                            curr_customer.setMonthly_package('2');
                            ConsumeMonthlyPackage(curr_customer);
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
                        if(BalanceEnough(curr_customer,superman)){
                            curr_customer.setMonthly_package('3');
                            ConsumeMonthlyPackage(curr_customer);
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
                        if(BalanceEnough(curr_customer,superman)){
                            curr_customer.setMonthly_package('3');
                            ConsumeMonthlyPackage(curr_customer);
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
                        if(BalanceEnough(curr_customer,normal)){
                            curr_customer.setMonthly_package('4');
                            ConsumeMonthlyPackage(curr_customer);
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
                        if(BalanceEnough(curr_customer,normal)){
                            curr_customer.setMonthly_package('4');
                            ConsumeMonthlyPackage(curr_customer);
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
    static public int ThirdMenu_2(SimCard curr_customer){
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
        if(curr_customer.getMonthly_package() instanceof NetWorm){
            ((NetWorm) curr_customer.getMonthly_package()).setDataPack(datapack);
            if(BalanceEnough(curr_customer,datapack*10)){
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
    static public int ThirdMenu_3(SimCard curr_customer){
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
    static public int ThirdMenu_4(SimCard curr_customer){
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
            //返回二级菜单
            return 2;
        }
    }
    static public void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        userScene.initializeUserScene();
        System.out.println("**********欢迎来到龙龙移动业务大厅**********");
        System.out.println("1、用户注册 2、用户登录 3、使用龙龙 4、退出系统");
        System.out.println("请输入您的选择: ");
        int first_menu = scanner.nextInt();
        first_menu = getInput(scanner,first_menu,4,1);

        if(first_menu == 1){
            System.out.println("**********龙龙为您挑了9个电话号码，请您选择**********");
            HashMap<Integer, String> randomTele = new HashMap<Integer, String>();
            String tele;
            Random r = new Random();
            for(int i = 0; i < 9; i++){
                tele = "189";
                for(int j = 0; j < 8; j++){
                    int bit = r.nextInt(10);
                    tele += Integer.toString(bit);
                }
                if(randomTele.containsValue(tele)){
                    i--;
                    continue;
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
            int telephone = Integer.parseInt(scanner.next());
            telephone = getInput(scanner, telephone, 9, 1);
            SimCard new_customer = new SimCard();
            new_customer.setTelephone(randomTele.get(telephone));
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
                System.out.println("密码设置成功");
            }
            else if(count > 3){
                System.out.println("密码多次输入不匹配，请重新注册");
                //返回上一级
            }
            System.out.println("1、话痨套餐，2、网虫套餐，3、超人套餐。请选择套餐(输入序号):");
            int pack_choice  = scanner.nextInt();
            pack_choice = getInput(scanner, pack_choice, 3, 1);
            pack_choice = (char) (pack_choice + 48);
            new_customer.setMonthly_package((char)pack_choice);
            System.out.println("请输入预存话费:");
            double money;
            try{
                money = scanner.nextDouble();
                if (money <= 0){
                    throw new MyException("预存金额必须大于零");
                }
                if(!BalanceEnough(new_customer,new_customer.getMonthly_package())){
                    throw new MyException("预存金额无法支付当前选择套餐的费用，请输入超过当前套餐包月"
                            +new_customer.getMonthly_package().fee+"元的话费");
                }
            }catch(MyException e){
                money = scanner.nextDouble();
            }
            new_customer.DepositBalance(money);
            customers.add_customer(new_customer);
            new_customer.printForNew();
            //返回上一级
        }

        if(first_menu == 2){
            System.out.println("请输入您的手机号: ");
            String phone_number = scanner.next();
            String in_password, re_password;
            System.out.println("请输入您的密码: (忘记密码请输入#)");
            //加一个找回密码的功能
            in_password = scanner.next();
            SimCard curr_customer = customers.getCustomer(phone_number);
            if(curr_customer.getPassword() == null){
                System.out.println("您还没有龙龙移动的电话卡，请尽快注册哟！");
                //返回上一级;
            }
            if(in_password.equals(curr_customer.getPassword())){
                System.out.println("输入密码成功！");
                System.out.println("**********龙龙很高兴能为您服务**********");
                System.out.println("1、业务办理 2、查询服务 3、退网服务 ");
                int service = scanner.nextInt();
                try{
                    if(service > 3 || service < 1){
                        throw new MyException("输入超界");
                    }
                }catch(MyException e){
                    e.toString();
                    //返回上一级
                }
                switch (service){
                    case 1: {
                        System.out.println("**********龙龙很高兴能为您办理业务**********");
                        System.out.println("1、主套餐 2、流量包 3、生活服务 4、话费充值 ");
                        int business = scanner.nextInt();
                        try{
                            if(business > 4 || business < 1){
                                throw new MyException("输入超界");
                            }
                        }catch(MyException e){
                            e.getMessage();
                            //返回上一级
                        }
                        switch (business) {
                            case 1: {
                                System.out.println("**********龙龙移动有以下几种套餐供您选择**********");
                                System.out.println("1、话痨套餐 2、网虫套餐 3、超人套餐 4、普通计费 ");
                                int Package = scanner.nextInt();
                                try{
                                    if(Package < 1 || Package > 4){
                                        throw new MyException("输出超界，返回上一级");
                                    }
                                }catch (MyException e){
                                    e.toString();
                                    //返回上一级
                                }
                                MonthlyPackage curr_package = curr_customer.getMonthly_package();
                                switch (Package) {
                                    case 1: {
                                        TalkingTooMuch talking = new TalkingTooMuch();
                                        talking.printPackage();
                                        if (curr_package.PackageNumber == '0') {
                                            System.out.println("您还没有选择套餐，您需要选择这个套餐吗？(y/n)");
                                            String choice = scanner.next();
                                            if (choice.equals("y")) {
                                                if(BalanceEnough(curr_customer,talking)){
                                                    curr_customer.setMonthly_package('1');
                                                    ConsumeMonthlyPackage(curr_customer);
                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
                                                }
                                            }
                                        } else if (curr_package.PackageNumber == 1) {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您正在查看您自己的套餐明细。");
                                            //返回上级
                                        } else {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您需要更换成这个套餐吗？(y/n)");
                                            String choice = scanner.next();
                                            if (choice.equals("y")) {
                                                if(BalanceEnough(curr_customer,talking)){
                                                    curr_customer.setMonthly_package('1');
                                                    ConsumeMonthlyPackage(curr_customer);
                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
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
                                                if(BalanceEnough(curr_customer,net)){
                                                    curr_customer.setMonthly_package('2');
                                                    ConsumeMonthlyPackage(curr_customer);
                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
                                                }
                                            }
                                        } else if (curr_package.PackageNumber == 2) {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您正在查看您自己的套餐明细。");
                                            //返回上级

                                        } else {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您需要更换成这个套餐吗？(y/n)");
                                            String choice = scanner.next();
                                            if (choice.equals("y")) {
                                                if(BalanceEnough(curr_customer,net)){
                                                    curr_customer.setMonthly_package('2');
                                                    ConsumeMonthlyPackage(curr_customer);
                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
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
                                                if(BalanceEnough(curr_customer,superman)){
                                                    curr_customer.setMonthly_package('3');
                                                    ConsumeMonthlyPackage(curr_customer);
                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
                                                }
                                            }
                                        } else if (curr_package.PackageNumber == 3) {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您正在查看您自己的套餐明细。");
                                            //返回上级

                                        } else {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您需要更换成这个套餐吗？(y/n)");
                                            String choice = scanner.next();
                                            if (choice.equals("y")) {
                                                if(BalanceEnough(curr_customer,superman)){
                                                    curr_customer.setMonthly_package('3');
                                                    ConsumeMonthlyPackage(curr_customer);
                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
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
                                                if(BalanceEnough(curr_customer,normal)){
                                                    curr_customer.setMonthly_package('4');
                                                    ConsumeMonthlyPackage(curr_customer);
                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
                                                }
                                            }
                                        } else if (curr_package.PackageNumber == 4) {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您正在查看您自己的套餐明细。");

                                        } else {
                                            System.out.print("您现在的套餐是:" + curr_package.PackageName);
                                            System.out.println(",您需要更换成这个套餐吗？(y/n)");
                                            String choice = scanner.next();
                                            if (choice.equals("y")) {
                                                if(BalanceEnough(curr_customer,normal)){
                                                    curr_customer.setMonthly_package('4');
                                                    ConsumeMonthlyPackage(curr_customer);

                                                }
                                                else{
                                                    System.out.println("您的余额不足，请先充值。");
                                                    //返回上级
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                            case 2:{
                                System.out.println("**********龙龙超大流量包等你来拿**********");
                                System.out.println("1、1GB 2、2GB 3、3GB ");
                                int datapack = scanner.nextInt();
                                try{
                                    if(datapack < 1 || datapack > 3){
                                        throw new MyException("输出超界，返回上一级");
                                    }
                                }catch (MyException e){
                                    e.toString();
                                    //返回上一级
                                }
                                if(curr_customer.getMonthly_package() instanceof NetWorm){
                                    ((NetWorm) curr_customer.getMonthly_package()).setDataPack(datapack);
                                    if(BalanceEnough(curr_customer,datapack*10)){
                                        curr_customer.ConsumeBalance(datapack*10);
                                    }
                                    else{
                                        System.out.println("您的余额不足，请先充值。");
                                        //返回上级
                                    }
                                }
                                else{
                                    System.out.println("您的套餐不支持超大流量包");
                                    //返回上级
                                }
                                break;
                            }
                            case 3:{
                                System.out.println("**********龙龙为您的生活增光添彩**********");
                                System.out.println("1、视频VIP 2、音乐VIP");
                                int entertaining = scanner.nextInt();
                                try{
                                    if(entertaining < 1 || entertaining > 3){
                                        throw new MyException("输出超界，返回上一级");
                                    }
                                }catch (MyException e){
                                    e.toString();
                                    //返回上一级
                                }
                                curr_customer.setLife_service(entertaining);
                                break;
                            }
                            case 4:{
                                System.out.println("**********话费不足，记得要及时充值噢，请输入充值金额**********");
                                double money = scanner.nextDouble();
                                int count = 1;
                                while (money < 50) {
                                    //throw new Exception("输入数据有误，请输入1-3");
                                    System.out.println("至少充值50元");
                                    money = scanner.nextDouble();
                                    count++;
                                    if(count > 10){
                                        break;
                                    }
                                }
                                if(count > 10) {
                                    System.out.println("多次输入错误，系统自动退出");
                                    //返回上级
                                }
                                else{
                                    curr_customer.DepositBalance(money);
                                    String deposit_record = curr_customer.getTelephone() + " 充值 " + money + "元";
                                    customers.add_deposit(curr_customer,deposit_record);
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case 2:{
                        System.out.println("**********龙龙很高兴能帮您查询**********");
                        System.out.println("1、本月账单打印 2、套餐余量查询 3、缴费记录查询 4、消费记录查询 5、资费说明");
                        int information = scanner.nextInt();
                        try{
                            if(information < 1 || information > 3){
                                throw new MyException("输出超界，返回上一级");
                            }
                        }catch (MyException e){
                            e.toString();
                            //返回上一级
                        }
                        switch(information){
                            case 1:{
                                printBill(curr_customer);
                                break;
                            }
                            case 2:{
                                printLeft(curr_customer);
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
                                FileWriter fw = new FileWriter("资费说明.txt");
                                try{
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    bw.write("----------资费说明----------");
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
                                }
                                fw.close();
                                break;
                            }
                        }
                        break;
                    }
                    case 3:{
                        System.out.println("**********龙龙很难过，你真的要选择退网吗(输入y/n)**********");
                        String back_off = scanner.next();
                        if(back_off.equals("y")){
                            customers.remove(phone_number);
                            System.out.println("感谢您的使用，希望未来您还能继续使用龙龙移动哦！");
                            break;
                        }
                        else{
                            //返回上一级
                            break;
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
                            System.out.println("密码修改成功");
                        }
                        else{
                            System.out.println("密码修改失败");
                        }
                    }

                }
                else{
                    //back to the front
                }
                System.out.println("");
            }
        }

        if(first_menu == 3){
            System.out.println("**********欢迎使用龙龙移动**********");
            System.out.println("请输入手机号:");
            String phone_number = scanner.next();
            SimCard curr_customer = customers.getCustomer(phone_number);

            if(curr_customer == null){
                System.out.println("号码不存在");
                //返回上一级
                return;
            }
            else {
                System.out.println("系统预设使用龙龙示例:");
                String[] action = userScene.getMode();
                assert action != null; //断言
                String mode = action[1];
                int number = Integer.parseInt(action[2]);
                System.out.println(action[0]);
                if (mode.equals("通话")) {
                    if (curr_customer.getMonthly_package() instanceof NetWorm) {
                        System.out.println("您的套餐不支持此项消费。");
                    }
                    else {
                        if (curr_customer.getMonthly_package() instanceof NormalPackage) {
                            String con = action[1] + " " + action[2];
                            customers.add_consumption(curr_customer, con);
                            double curr_fee = curr_customer.getMonthly_package().fee;
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                        if (curr_customer.getMonthly_package() instanceof TalkingTooMuch) {
                            TalkingTooMuch p = (TalkingTooMuch) curr_customer.getMonthly_package();
                            p.call(number);
                            double curr_fee = p.fee;
                            String con = action[1] + " " + action[2];
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
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                        if (curr_customer.getMonthly_package() instanceof Superman) {
                            Superman p = (Superman) curr_customer.getMonthly_package();
                            double curr_fee = p.fee;
                            p.call(number);
                            String con = action[1] + " " + action[2];
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
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }

                if(mode.equals("短信")){
                    if(curr_customer.getMonthly_package() instanceof NetWorm){
                        System.out.println("您的套餐不支持此项消费。");
                    }
                    else{
                        if(curr_customer.getMonthly_package() instanceof NormalPackage){
                            String con = action[1]+" "+action[2];
                            customers.add_consumption(curr_customer, con);
                            double curr_fee = curr_customer.getMonthly_package().fee;
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                        if(curr_customer.getMonthly_package() instanceof TalkingTooMuch){
                            TalkingTooMuch p = (TalkingTooMuch) curr_customer.getMonthly_package();
                            double curr_fee = p.fee;
                            String con = action[1]+" "+action[2];
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
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                        if(curr_customer.getMonthly_package() instanceof Superman){
                            Superman p = (Superman) curr_customer.getMonthly_package();
                            double curr_fee = p.fee;
                            String con = action[1]+" "+action[2];
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
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(mode.equals("流量")){
                    if(curr_customer.getMonthly_package() instanceof TalkingTooMuch){
                        System.out.println("您的套餐不支持此项消费。");
                    }
                    else{
                        if(curr_customer.getMonthly_package() instanceof NormalPackage){
                            String con = action[1]+" "+action[2];
                            double curr_fee = curr_customer.getMonthly_package().fee;
                            customers.add_consumption(curr_customer, con);
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                        if(curr_customer.getMonthly_package() instanceof NetWorm){
                            NetWorm p = (NetWorm) curr_customer.getMonthly_package();
                            double curr_fee = p.fee;
                            String con = action[1] + " " + action[2];
                            customers.add_consumption(curr_customer, con);
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
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                        if(curr_customer.getMonthly_package() instanceof Superman){
                            Superman p = (Superman) curr_customer.getMonthly_package();
                            String con = action[1]+" "+action[2];
                            customers.add_consumption(curr_customer, con);
                            double curr_fee = p.fee;
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
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
                System.out.println("用户自定义模式:");
                MonthlyPackage m = curr_customer.getMonthly_package();
                double curr_fee = 0;
                if(m instanceof TalkingTooMuch){
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
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
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
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                if(m instanceof NetWorm){
                    System.out.println("请输入您的上网耗费流量");
                    int data = scanner.nextInt();
                    curr_fee = curr_customer.UseTelephone("上网",data);
                    System.out.println("用户"+curr_customer.getTelephone()+"上网使用了了"+data+"MB流量");
                    try{
                        if(!BalanceEnough(curr_customer,curr_fee)){
                            System.out.println("您的余额不足，请尽快充值！");
                            throw new MyException("您的余额不足，请尽快充值！");
                        }
                    } catch(MyException e){
                        e.printStackTrace();
                    }
                }
                if(m instanceof Superman){
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
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
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
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                            break;
                        }
                        case 3:{
                            System.out.println("请输入您的上网耗费流量");
                            int data = scanner.nextInt();
                            curr_fee = curr_customer.UseTelephone("上网",data);
                            System.out.println("用户"+curr_customer.getTelephone()+"上网使用了了"+data+"MB流量");
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
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
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
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
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                            break;
                        }
                        case 3:{
                            System.out.println("请输入您的上网耗费流量");
                            int data = scanner.nextInt();
                            curr_fee = curr_customer.UseTelephone("上网",data);
                            System.out.println("用户"+curr_customer.getTelephone()+"上网使用了了"+data+"MB流量");
                            try{
                                if(!BalanceEnough(curr_customer,curr_fee)){
                                    System.out.println("您的余额不足，请尽快充值！");
                                    throw new MyException("您的余额不足，请尽快充值！");
                                }
                            } catch(MyException e){
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }

        if(first_menu == 4){
            System.out.println("谢谢，欢迎下次光临！");
        }
    }

    static public int getInput(Scanner scanner, int input, int up, int down) throws Exception{
        try{
            int count = 1;
            while (input < down || input > up) {
                System.out.println("输入有误，请输入数字"+down+"-"+up);
                input = scanner.nextInt();
                count++;
                if(count > 10){
                    break;
                }
            }
            if(count > 10) {
                System.out.println("多次输入错误，系统已自动退出");
                return -1;
            }
            System.out.println("输入数据有误，请输入"+down+"-"+up);
            throw new MyException("输入数据有误，请输入"+down+"-"+up);
        }catch(MyException e){
            e.getMessage();
        }
        return input;
    }
    private static void printBill(SimCard s){
        DecimalFormat formatDouble = new DecimalFormat("#.00");
        MonthlyPackage p = s.getMonthly_package();
        double this_month_fee = 0;
        if(p instanceof TalkingTooMuch){
            TalkingTooMuch talkingTooMuch = (TalkingTooMuch) p;
            talkingTooMuch.printPackageUse(s);
            this_month_fee = talkingTooMuch.fee;
        }
        if(p instanceof NetWorm){
            NetWorm netWorm = (NetWorm) p;
            netWorm.printPackageUse(s);
            this_month_fee = netWorm.fee;
        }
        if(p instanceof Superman){
            Superman superman = (Superman) p;
            superman.printPackageUse(s);
            this_month_fee = superman.fee;
        }
        if(p instanceof NormalPackage){
            NormalPackage normalPackage = (NormalPackage) p;
            normalPackage.printPackageUse(s);
            this_month_fee = normalPackage.fee;
        }
        if(s.getBalance() >= this_month_fee){
            String format_left = formatDouble.format(s.getBalance()-this_month_fee);
            System.out.println("本月余额:"+format_left+"元");
        }
        else{
            System.out.println("本月余额:"+0+"元");
            String format_left = formatDouble.format(s.getBalance()-this_month_fee);
            System.out.println("本月欠费:"+format_left+"元");
        }

    }

    private static void printLeft(SimCard s){
        MonthlyPackage p = s.getMonthly_package();
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

    static private boolean BalanceEnough(SimCard s, MonthlyPackage p){
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
    static private boolean BalanceEnough(SimCard s, double num){
        return s.getBalance() >= num;
    }
    static private void ConsumeMonthlyPackage(SimCard s){
        MonthlyPackage p = s.getMonthly_package();
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
    }
}
*/