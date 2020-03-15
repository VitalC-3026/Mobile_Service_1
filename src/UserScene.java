import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class UserScene {
    static private Map<Integer, String[]> user_scene = new HashMap<>();
    static void initializeUserScene(){
        String[][] scene ={
                {"问候客户，谁知其如此难缠 通话90分钟","通话","90"},
                {"通知朋友手机换号 发送短信100条","短信","100"},
                {"参与环境保护实施方案问卷调查 发送短信5条","短信","5"},
                {"看4k高清粤语版国庆阅兵 上网用流量","流量","5"},
                {"发微博控诉作业很多的Java课程，被网友教育要好好学习 上网用流量","流量","1"},
                {"和父母聊天 通话40分钟","通话","40分钟"}
        };
        for(int i = 0; i < 6; i++){
            user_scene.put(i+1,scene[i]);
        }
    }
    static String[] getMode(){
        Random r = new Random();
        int UserMode = r.nextInt(6);
        while(UserMode < 1 || UserMode > 6){
            UserMode = r.nextInt(6);
        }

        return user_scene.get(UserMode);
    }
}
