package byow.InputDemo;

import edu.princeton.cs.introcs.StdDraw;

public class KeyboardInput implements InputSource {
     private static final boolean PRINT_TYPED_KEYS = false;
     public KeyboardInput() {
         StdDraw.setCanvasSize(1280, 640);
         StdDraw.clear(StdDraw.BLACK);
         StdDraw.setPenColor(StdDraw.WHITE);
         StdDraw.text(0.5, 0.6, "CS61B: THE GAME");
         StdDraw.text(0.5, 0.5, "NEW GAME(N)");
         StdDraw.text(0.5, 0.4, "LOAD GAME(L)");
         StdDraw.text(0.5, 0.3, "QUIT(Q)");
     }

     public char getNextKey() {
         while (true) {
             if (StdDraw.hasNextKeyTyped()) {
                 char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                 if (PRINT_TYPED_KEYS) {
                     System.out.print(c);
                 }
                 return c;
             }
         }
     }

     public boolean possibleNextInput() {
            return true;
        }
}

