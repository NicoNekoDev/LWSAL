package ro.nicuch.lwsal;

import org.junit.Assert;
import org.junit.Test;
import ro.nicuch.lwsal.options.AnimationOptions;
import ro.nicuch.lwsal.types.Animation;
import ro.nicuch.lwsal.types.AnimationReverse;
import ro.nicuch.lwsal.types.AnimationScroller;
import ro.nicuch.lwsal.types.AnimationText;

public class AnimationTest extends Assert {

    @Test
    public void testJsonAnimation() {
        String json = "[\n" +
                "    \"&cMessage: \",\n" +
                "    {\n" +
                "        \"type\": \"scroller\",\n" +
                "        \"options\": {\n" +
                "            \"update_time\": 5,\n" +
                "            \"space_between\": 5,\n" +
                "            \"display_size\": 15\n" +
                "        },\n" +
                "        \"text\": [\n" +
                "            \"&eHello \",\n" +
                "            {\n" +
                "                \"type\": \"rainbow\",\n" +
                "                \"options\": {\n" +
                "                    \"update_time\": 2,\n" +
                "                    \"strip_colors\": true\n" +
                "                },\n" +
                "                \"text\": \"%player_name%\"\n" +
                "            },\n" +
                "            \"&e!\"\n" +
                "        ]\n" +
                "    }\n" +
                "]";
        Animation animation = AnimationsManager.readJsonString(json);
        //First message test
        assertEquals("&cMessage: &eHello &0%player_n", animation.getText());
        //Update and get the text 25 times (or 25 ticks), checking for index error.
        for (int i = 0; i < 25; i++) {
            animation.update();
            animation.getText();
        }
        //The text after 25 times (or 25 ticks)
        assertEquals("&cMessage: &e &c%player_name%&e!&e", animation.getText());
    }

    @Test
    public void testReverseAnimationNull() {
        AnimationReverse animationReverse = new AnimationReverse(new AnimationText(null), null);
        animationReverse.update();
        assertEquals("", animationReverse.getText());
    }

    @Test
    public void testReverseAnimationBig() {
        AnimationReverse animationReverse = new AnimationReverse(new AnimationText("&a123 &b1234 &c12345 &d123456"), new AnimationOptions());
        animationReverse.update();
        assertEquals("&d654321&c 54321&b 4321&a 321", animationReverse.getText());
    }

    @Test
    public void testReverseAnimationSmall() {
        AnimationReverse animationReverse = new AnimationReverse(new AnimationText("&a123 &b1234"), new AnimationOptions());
        assertEquals("&b4321&a 321", animationReverse.getText());
    }

    @Test
    public void testScrollerAnimationNull() {
        AnimationScroller animationScroller = new AnimationScroller(new AnimationText(null), null);
        animationScroller.update();
        assertEquals("", animationScroller.getText());
    }
}
