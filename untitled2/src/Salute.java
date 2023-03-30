
public class Salute {
    public static int solution(String str) {

        State state = new State();
        String hallwayCleared = str.replace("-","");

        for (int i = 0 ; i < hallwayCleared.length() ; i++){
            char c = hallwayCleared.charAt(i);


            if (state.isLeftSaluter(c)){
                state.leftSaluter++;
                System.out.println("ls"+state.leftSaluter);
            }else {
                state.updateSaluteCount();
            }
        }
        return state.saluteCount;
    }


    private static class State{
        int leftSaluter = 0;
        int saluteCount = 0;

        public boolean isLeftSaluter(char sign){
            return sign == '>';
        }

        public void updateSaluteCount(){
            saluteCount += leftSaluter*2;
        }
    }
}

