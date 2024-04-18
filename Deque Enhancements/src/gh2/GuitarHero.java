package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";


    public static void main(String[] args) {

        ArrayList<GuitarString> keys = new ArrayList<>();
        for (char i = 0; i < KEYBOARD.length(); i++) {
            GuitarString current = new GuitarString(400 * Math.pow(2, (i - 24.0) / 12.0));
            keys.add(current);
        }


        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);
                if (index != -1) {
                    keys.get(index).pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < KEYBOARD.length(); i++) {
                sample += keys.get(i).sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < KEYBOARD.length(); i++) {
                keys.get(i).tic();
            }
        }
    }
}

