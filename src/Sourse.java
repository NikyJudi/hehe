import java.util.Arrays;
import java.util.Scanner;
public class Sourse {
    public static void main( String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入资源数m");
        int m = scanner.nextInt();
        System.out.println("请输入进程数n");
        int n = scanner.nextInt();
        int[] available = new int[m];
        System.out.println("请输入各资源名和资源数");
        System.out.println("资源名   " + "  资源数");
        String[] name = new String[m];
        for (int i = 0; i < m; i++) {
            name[i] = scanner.next();
            available[i] = scanner.nextInt();
        }
        System.out.println("资源输入完成");

        System.out.println("请输入进程的最大需求");
        int[][] max = new int[n][m];
        for (int i = 0; i < n; i++) {
            System.out.println("请输入第" + i + "个进程的" + m + "个最大需求");
            for (int j = 0; j < m; j++) {
                System.out.print(name[j] + "   ");
                max[i][j] = scanner.nextInt();
            }
        }
        System.out.println("进程最大需求输入完成");
        System.out.println("请输入各进程已分配的资源数");
        int[][] allocation = new int[n][m];
        for (int i = 0; i < n; i++) {
            System.out.println("请输入第" + i + "个进程的" + m + "个资源已经分配的数");
            for (int j = 0; j < m; j++) {
                System.out.print(name[j] + "   ");
                allocation[i][j] = scanner.nextInt();
            }
        }
        System.out.println("已分配资源输入完成");
        int[][] need = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
        //System.out.println(Arrays.toString(available));
        /*for (int i = 0; i < n; i++) {
            System.out.println(Arrays.toString(max[i]));
        }
        for (int i = 0; i < n; i++) {
            System.out.println(Arrays.toString(allocation[i]));
        }
        for (int i = 0; i < n; i++) {
            System.out.println(Arrays.toString(need[i]));
        }*/

        boolean[] finish = new boolean[n];
        for (int i = 0; i < finish.length; i++) {
            finish[i] = true;
        }
        bank(n, m, available, finish, need, allocation,name);
        scanner.close();
    }
    private static boolean check(int n, int m, int[] work,
                                 boolean[] finish, int[][] need, int[][] allocation) {
        boolean flag = true;
        boolean result = false;
        int index = 0;
        while (flag){
            if (index == n) {
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    if (finish[i]) {
                        sum++;
                    }
                }
                result = sum == 0;
                flag = false;
            }
            for (int i = 0; i < n; i++) {
                if (finish[i]) {
                    boolean flag1 = true;
                    for (int j = 0; j < m; j++) {
                        if (need[i][j] > work[j]) {
                            flag1 = false;
                        }
                    }
                    if (flag1) {
                        for (int j = 0; j < m; j++) {
                            work[j] += allocation[i][j];
                        }
                        finish[i] = false;
                    }
                } // end if
            } // end for
            index++;
        }
        return result;
    } // 3 5 a 10 b 5 c 7 7 5 3 3 2 2 9 0 2 2 2 2 4 3 3 0 1 0 2 0 0 3 0 2 2 1 1 0 0 2
    private static void bank(int n, int m, int[] available, boolean[] finish,
                             int[][] need, int[][] allocation, String[] name) {
        Scanner scanner = new Scanner(System.in);
        int[] request = new int[m];
        boolean[] isEnd = new boolean[n];//判断进程是否已经执行完毕
        for (int i = 0; i < n; i++) {
            isEnd[i] = false;
        }
        //进行进程调度
        while (true) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    sum += need[i][j];
                }
            }
            if (sum == 0) {
                System.out.println("调度完毕！");
                break;
            }
            int index = 0;
            while (isEnd[index]) {
                index++;
            }
            System.out.println("进程" + index + "还需要资源种类");
            System.out.println("资源名称     需求资源数目");
            for (int i = 0; i < m; i++) {
                System.out.println( name[i] + "  " + "  " + need[index][i]);
            }
            System.out.println("请输入申请各类资源的数量");
            System.out.println("资源名称     申请资源数目");
            for (int i = 0; i < m; i++) {
                System.out.print(name[i] + "  ");
                request[i] = scanner.nextInt();
                while (need[index][i] < request[i]) {
                    System.out.println("请重新输入" + name[i] + "类资源的申请数目");
                    request[i] = scanner.nextInt();
                }
                if (request[i] > available[i]) {
                    System.out.println( name[i] + "类资源仅有" + available[i]);
                    System.out.println("进程" + index + "等待");
                    request[i] = 0;
                } else {
                    available[i] = available[i] - request[i];
                    allocation[index][i] += request[i];
                    need[index][i] -= request[i];
                }
            }
            boolean safe = check(n,m,available,finish,need,allocation);
            if (safe) {
                //安全
                System.out.println("success！");
            } else {
                //不安全
                System.out.println("不安全 !");
                for (int i = 0; i < n; i++) {
                    //系统把资源分配给某进程
                    available[i] = available[i] + request[i];
                    allocation[index][i] -= request[i];
                    need[index][i] += request[i];
                }
            }
            int needNum = 0;//进程需要的资源数
            for (int i = 0; i < m; i++) {
                needNum += need[index][i];
            }
            if (needNum == 0) {
                //进程结束
                System.out.println("进程" + index + "结束");
                isEnd[index] = true;
                for (int i = 0; i < m; i++) {
                    //资源处理
                    available[i] += allocation[index][i];
                    allocation[index][i] = 0;
                }
            }
        }
        scanner.close();
    }
}
