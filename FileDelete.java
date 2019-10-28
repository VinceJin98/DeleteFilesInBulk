import java.io.File;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;

/**
 * 这个文件来删除指定的文件类型一段时间不用到的文件
 * @author vince_jin
 * @version 1.0
 */
public class FileDelete{
    static String dirname;      //路径名
    static String fileType;     //文件类型
    static int time;            //修改时间
    static Scanner in = new Scanner(System.in); //打开一个扫描器

    /**
     * 删除目标文件下 指定类型文件中 超过相应时间的文件
     * @return false代表没有需要删除的文件 true代表删除成功
     */
    public static boolean deleteFile() {
        File f1 = new File(dirname);
        Stack<File> stack = new Stack<File>(); //存储一个目录栈

        if(f1.isDirectory()){//是路径名则删除路径下所有符合的文件
            while(f1.isDirectory()){ //循环
                File[] ChildFile = f1.listFiles();
                for(final File element:ChildFile){//如果是目录则压栈,是文件则判断
                    System.out.println(element.getAbsolutePath());
                    if(element.isDirectory())
                        stack.push(element);
                    else{
                        if(patternCheck(element.getAbsolutePath()) && timeCheck(element.getAbsolutePath()) && DeleteConfirm(element.getAbsolutePath())) //如果符合删除条件则删除
                            element.delete(); 
                    }
                }
                if(!stack.empty())
                    f1 = stack.pop();
                else //没有子文件夹
                    return true;
            }
        }else{ //如果直接是文件则直接匹配
            if(patternCheck(dirname) && timeCheck(dirname) && DeleteConfirm(dirname)){
                f1.delete(); 
            } //如果符合删除条件则删除
        }
        return false;
    }

    /**
     * 确认是否要删除文件
     * @param FilePath 要删除文件的决定路径
     * @return true代表需要删除;false代表不进行删除
     */
    public static boolean DeleteConfirm(String FilePath){
        System.out.println("正在删除" + FilePath + " y或默认表示确认:");
        String y = "";
        y = in.nextLine();
        if( y.equals("y") || y.equals("")){
            return true;
        }
        return false; 
    }
    /**
     * 匹配文件类型
     * @param FileName 需要匹配的文件名
     * @return 如果类型符合返回true;如果类型不符合则返回true
     */
    public static boolean patternCheck(final String FileName){
        return FileName.endsWith("."+fileType);
    }

    /**
     * 匹配文件是否在可以容忍的时间之外
     * @param FileName 需要匹配的文件名
     * @return 如果符合则返回true；如果不符合则返回false
     */
    public static boolean timeCheck(final String FileName){
        File f = new File(FileName);
        return (f.lastModified() + time) <= (new Date().getTime());
    }

    /**
     * 主函数
     * @param args args[0] 路径; args[1] 类型; args[2] 时间 (单位:天);
     */
    public static void main(String args[]){
        dirname = args[0];
        fileType = args[1];
        time = Integer.parseInt(args[2]) * 24 * 60 * 60 * 1000; //换算成毫秒

        deleteFile();
        in.close();
    }
}