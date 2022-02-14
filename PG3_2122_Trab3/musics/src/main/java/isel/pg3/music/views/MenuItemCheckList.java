package isel.pg3.music.views;

import isel.pg3.music.model.MusicItem;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.*;

/**
 * this menu maintains a check list of the selected genres
 */
public class MenuItemCheckList extends JMenu {
     private Collection<String> selected;
     public ArrayList<String> aL = new ArrayList<>();
     public MenuItemCheckList(String title) {
         super(title);
         selected = new HashSet<>();
     }

     private void itemListener(ItemEvent e) {
         JCheckBoxMenuItem src =
             (JCheckBoxMenuItem) e.getSource();
         if (e.getStateChange() == ItemEvent.DESELECTED) {
             selected.remove(src.getText());
         }
         else {
             selected.add(src.getText());

         }
     }

     public MenuItemCheckList addItem(String text, ActionListener a) {
         var mi = new JCheckBoxMenuItem(text);
         if(aL.contains(text)!= true)
            aL.add(text);

         mi.addItemListener(this::itemListener);
         mi.addActionListener(a);
         add(mi);
         return this;
     }

     public String[] getSelected() {
         return selected.toArray(sz -> new String[sz]);
     }

     public boolean isItem(String s){
         for(String string: aL){
             if(string.equals(s))
                return true;

         }
         return false;

    }
     public boolean isItem(String ... s){
         for(String a : s) {
             for ( String b : aL) {
                 if (a.equals(b))
                     return true;
             }
         }
         return false;

    }

    public boolean isItem(List<MusicItem> a){
        for( MusicItem i : a) {
            isItem(i.getGenre());
        }
        return false;
    }

}
