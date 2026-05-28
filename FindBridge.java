import java.util.*;

class Edge{
    int u, v;

    public Edge(int u, int v){ //같은 간선을 중복으로 추가하지 않기 위해 최소값과 최대값을 저장
        this.u = Math.min(u, v);
        this.v = Math.max(u, v);
    }

    @Override
    public boolean equals(Object obj){ //간선으로 연결된 것인지 확인하기 위해 서로의 값을 비교교
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return u == edge.u && v == edge.v;
    }

    @Override
    public int hashCode(){ //해쉬값 생성
        return Objects.hash(u, v);
    }

    @Override
    public String toString(){ //(u, v) 형식으로 출력
        return "(" + u + ", " + v + ")";
    }
}

class Graph{
    int V;
    LinkedList<Integer>[] adj;

    public Graph(int V){ //그래프 초기화
        this.V = V;
        adj = new LinkedList[V];
        for(int i = 0; i < V; i++)
            adj[i] = new LinkedList<>();
    }
    
    public void addEdge(int u, int v){ //연결되는 정점의 리스트에 서로를 추가
        adj[u].add(v);
        adj[v].add(u);
    }

    int getV() { //정점의 개수 반환
        return V;
    }

    LinkedList<Integer> getAdj(int u) { //연결되는 정점의 리스트 반환
        return adj[u];
    }

}

class BridgeFinder{
    int time;
    int[] disc, low;
    boolean[] visited;
    Graph graph;
    Set<Edge> bridges;

    public BridgeFinder(Graph graph){ //그래프 초기화
        this.time = 0;
        int V = graph.getV();
        this.disc = new int[V];
        this.low = new int[V];
        this.visited = new boolean[V];
        this.graph = graph;
        this.bridges = new HashSet<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        Arrays.fill(visited, false);
    }

    void execute(){ //알고리즘 실행
        for(int i = 0; i < graph.getV(); i++){
            if(!visited[i]){
                dfs(i, -1);
            }
        }
    }

    void dfs(int u, int parent){//깊이 우선 탐색 알고리즘
        visited[u] = true;
        low[u] = time++;
        disc[u] = low[u];
        LinkedList<Integer> adj = graph.getAdj(u);
        for(int v : adj){
            if(v == parent) continue;
            if(!visited[v]){
                dfs(v, u);
                low[u] = Math.min(low[u], low[v]);

                if(disc[u]<low[v]){
                    bridges.add(new Edge(u, v));
                }
            } else {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        
        
    }
    
    Set<Edge> getBridges(){
        return bridges;
    }

    boolean isBridge(int u, int v){
        return bridges.contains(new Edge(u, v));
    }
}

public class FindBridge{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("===== [브리지 찾기 프로그램] =====");

        System.out.print("정점의 개수를 입력하세요 : ");
        int V = sc.nextInt();

        Graph graph = new Graph(V);

        System.out.println("간선의 양 끝점 쌍을 입력하세요 : (-1, -1) 입력 시 종료");
        while(true){
            int u = sc.nextInt();
            int v = sc.nextInt();
            if(u == -1 && v == -1) break;
            if(u < 0 || v < 0 || u >= V || v >= V) 
            {
                System.out.println("잘못된 입력입니다. 다시 입력하세요.");
                continue;
            }
            graph.addEdge(u, v);
        }

        BridgeFinder finder = new BridgeFinder(graph);
        finder.execute();
        System.out.println("그래프 생성 완료 ");
       
        while (true){
            System.out.println(" -------------------------------");
            System.out.println("1. 모든 브리지 찾기");
            System.out.println("2. 특정 간선 판별");
            System.out.println("3. 프로그램 종료");
            System.out.println("메뉴를 선택하세요 : ");

            int choice = sc.nextInt();
            
            if (choice == 1) {
                Set<Edge> bridges = finder.getBridges();
                System.out.println("\n--- [모든 브리지 찾기 결과] ---");
                if (bridges.isEmpty()) {
                    System.out.println("그래프에 브리지가 존재하지 않습니다.");
                } else {
                    for (Edge bridge : bridges) {
                        System.out.println(bridge);
                    }
                }
            }
            else if (choice == 2) {
            // 2번 메뉴: 특정 간선 판별
                System.out.print("\n판별할 간선의 양 끝점을 입력하세요 (u v): ");
                int targetU = sc.nextInt();
                int targetV = sc.nextInt();
            
                if (targetU < 0 || targetU >= V || targetV < 0 || targetV >= V) {
                    System.out.println("잘못된 정점 번호입니다.");
                    continue;
                }

            // 이미 연산된 결과를 바탕으로 O(1) 속도로 즉시 판별
                if (finder.isBridge(targetU, targetV)) {
                    System.out.println(" 간선 (" + targetU + ", " + targetV + ")는 브리지가 맞습니다.");
                } 
                else {
                    System.out.println(" 간선 (" + targetU + ", " + targetV + ")는 브리지가 아닙니다.");
                } 
            }
                else if (choice == 3) {
            // 3번 메뉴: 종료
                    System.out.println("\n프로그램을 종료합니다.");
                    break;
            
                } else {
            // 잘못된 입력 처리
                    System.out.println("\n 잘못된 입력입니다. 1~3 사이의 번호를 선택해주세요.");
                }
        }
    
        sc.close();
    }
}