import java.util.PriorityQueue;
public class SolutionS
{

    // global variables
    public static int ans = Integer.MAX_VALUE;
    public static int dx [] = {0,0,-1,1};
    public static int dy [] = {-1,1,0,0};

    static class State implements Comparable<State>
    {
        int x,y,moves;
        boolean wentThroughWall;
        public State(int x, int y, int moves,  boolean wentThroughWall)
        {
            this.x = x;
            this.y = y;
            this.moves = moves;
            this.wentThroughWall = wentThroughWall;
        }

        public int compareTo(State other)
        {
            return moves - other.moves;
        }
    }

    public static int solution(int[][] map)
    {
        PriorityQueue<State> enque = new PriorityQueue<State>();
        boolean visited [][][] = new boolean [map.length][map[0].length][2];

        enque.add(new State(0, 0, 1,false));
        while(!enque.isEmpty())
        {
            State top = enque.poll();
            if(top.x == map.length - 1 && top.y == map[0].length - 1)
            {
                ans = Math.min(ans, top.moves);
                continue;
            }

            if(visited[top.x][top.y][(top.wentThroughWall ? 0 : 1)])
                continue;

            visited[top.x][top.y][(top.wentThroughWall ? 0 : 1)] = true;

            for(int i = 0; i < dx.length; i++)
            {
                int nx = top.x + dx[i];
                int ny = top.y + dy[i];
                if(nx < 0 || nx >= map.length || ny < 0 || ny >= map[0].length || (map[nx][ny] == 1 && top.wentThroughWall))
                    continue;

                if(map[nx][ny] == 1)
                    enque.add(new State(nx, ny, top.moves + 1, true));
                else
                    enque.add(new State(nx, ny, top.moves + 1, top.wentThroughWall));
            }
        }

        assert(ans != Integer.MAX_VALUE);
        return ans;
    }

    public static void main(String[] args) {

        int [][] test = {
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
};
        System.out.println(SolutionS.solution(test));
    }

}