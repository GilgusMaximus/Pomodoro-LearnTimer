
import org.omg.CORBA.TIMEOUT;

import java.awt.Toolkit;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.awt.Toolkit.*;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

public class Timer {
  private static long arbeitsZeit = 45;
  private static long kurzePause = 5;
  private static long langePause = 10;
  private static long arbeitsIntervallCounter = 0;
  private static final long arbeitsZeitStandard = 45;
  private static final long kurzePauseStandard = 5;
  private static final long langePauseStandard = 10;

  private static void createNotification(String notificationText) throws AWTException, MalformedURLException{
    if (SystemTray.isSupported()) {
      Timer td = new Timer();
      td.displayTray(notificationText);
    } else {
      System.err.println("System tray not supported!");
    }
  }
  private static int startMenu(Scanner scanner){
    String eingabe = "";
    //StartmenuAusgabe
    clearSpace();
    System.out.println("     Willkommen zum Pomodoro LearnTimer\n \t -+-+-+-+-+-+-+-+-+-+-+-+");
    System.out.println("Bitte waehlen Sie eine Option mit Eingabe der entsprechende Zahl aus");
    System.out.println("0) Lernsession beginnen\n1) Optionen\n2) Beenden");

    while(true){
      if(scanner.hasNextInt()) {
        if (eingabe.compareTo("0") != 0 || eingabe.compareTo("1") != 0 || eingabe.compareTo("2") != 0) {
          eingabe = scanner.nextLine();
          break;
        }
      }
      System.out.println("Bitte einen ganzzahlien Wert eingeben");
      scanner.next();
    }

    //Because the actual state values are arranged in another way
    if(eingabe.compareTo("0") == 0)
      return 1;
    else if(eingabe.compareTo("1") == 0)
      return 2;
    else if(eingabe.compareTo("2") == 0)
      return -1;
    else
      return 0;
  }

  private static int optionsMenu(Scanner scanner){
    scanner = new Scanner(System.in);
    String eingabe = "";

    clearSpace();
    //printSperationLine();
    System.out.println("     \tOptionsMenu");
    printSperationLine();
    System.out.println("Bitte waehlen Sie eine Option mit Eingabe der entsprechende Zahl aus");
    System.out.println("0) Dauer der Lernsessions bearbeiten");
    System.out.println("1) Dauer der kurzen Pausen bearbeiten");
    System.out.println("2) Dauer der langen Pausen bearbeiten");
    System.out.println("3) Auf Standard zuruecksetzen");
    System.out.println("4) Zurück zum Startmenu");

    while(true){
      if(scanner.hasNextInt()) {
        if (eingabe.compareTo("0") != 0 || eingabe.compareTo("1") != 0 || eingabe.compareTo("2") != 0 || eingabe.compareTo("3") == 0 || eingabe.compareTo("4") == 0) {
          eingabe = scanner.nextLine();
          break;
        }
      }
      System.out.println("Bitte einen ganzzahlien Wert eingeben");
      scanner.nextLine();
    }

    if(eingabe.compareTo("0") == 0){
      System.out.println("Bitte neue Zeit in ganzen Minuten eingeben: ");
        while(!scanner.hasNextInt()){
          scanner.next();
          System.out.println("Bitte einen ganzen Wert eingeben");
        }
        arbeitsZeit = scanner.nextInt();
        System.out.println("Ihre neue Arbeitszeit beträgt " + arbeitsZeit +"min");
    }else if(eingabe.compareTo("1") == 0){
      System.out.println("Bitte neue Zeit in ganzen Minuten eingeben: ");
      while(!scanner.hasNextInt()){
        scanner.next();
        System.out.println("Bitte einen ganzen Wert eingeben");
      }
      kurzePause = scanner.nextInt();
      System.out.println("Ihre neue kurze Pause beträgt " + kurzePause + "min");
    }else if(eingabe.compareTo("2") == 0){
      System.out.println("Bitte neue Zeit in ganzen Minuten eingeben: ");
      while(!scanner.hasNextInt()){
        scanner.next();
        System.out.println("Bitte einen ganzen Wert eingeben");
      }
      langePause = scanner.nextInt();
      System.out.println("Ihre neue lange Pause beträgt " + langePause + "min");
    }else if(eingabe.compareTo("3") == 0){
      arbeitsZeit = arbeitsZeitStandard;
      kurzePause = kurzePauseStandard;
      langePause = langePauseStandard;
    }else{
      return 0;
    }
    return 2;
  }
  private static int learnSession(){
    Scanner scanner = new Scanner(System.in);
    String eingabe = "";
    //CMD Output
    System.out.println("Arbeitszeit von "+ arbeitsZeit +"min beginnt nun");
    //Win10 Notification
    try {
      createNotification("Die Arbeitszeit von "+ arbeitsZeit + "min beginnt nun");
    }catch(Exception e){
    }
    createWindowsPing();
    while (eingabe.compareTo("-1") != 0) {
      try {
        TimeUnit.MINUTES.sleep(arbeitsZeit);
      } catch (Exception e) {
      }
      try {
        createNotification("Die Arbeitszeit ist vorbei - Pausenwahl möglich");
      }catch(Exception e){
      }

      createWindowsPing();
      arbeitsIntervallCounter = arbeitsIntervallCounter +1;
      long pausenZeit = 0;
      if(arbeitsIntervallCounter == 3) {
        System.out.println("Möchtest du eine Pause von " + langePause
                + " einlegen? y = 1/n = 0. Oder möchten Sie zum Startmenu? y = 2. Oder möchten Sie beenden? y = -1");
        pausenZeit = kurzePause;
      } else {
        System.out.println("Möchtest du eine Pause von " + kurzePause
                + " einlegen? y = 1/n = 0. Oder möchten Sie zum Startmenu? y = 2. Oder möchten Sie beenden? y = -1");
        pausenZeit = langePause;
      }

      while(true){
        if(scanner.hasNextInt()){
          eingabe = ""+scanner.nextInt();
          if(eingabe.compareTo("-1") == 0 || eingabe.compareTo("0") == 0 || eingabe.compareTo("1") == 0 || eingabe.compareTo("2") == 0){
            break;
          }
        }
      }

      if(eingabe.compareTo("1") == 0){
        System.out.println("Pause beginnt jetzt mit Länge " + pausenZeit);
        try {
          TimeUnit.MINUTES.sleep(pausenZeit);
        } catch (Exception e) {

        }
      }else if(eingabe.compareTo("0") == 0) {
        System.out.println("Du hast die Pause geskippt. Nächste beginnt in "+ arbeitsZeit + "min");
      }else if(eingabe.compareTo("2") == 0) {
        System.out.println("Kehre zum Startmenu zurück...");
        try {
          TimeUnit.SECONDS.sleep(2);
        }catch(Exception e){
        }
        return 0;
      }else
      {
        return -1;
      }
    }
    return 0;
  }

  static private void displayTray(String notificationText) throws AWTException, MalformedURLException {
    //Obtain only one instance of the SystemTray object
    SystemTray tray = SystemTray.getSystemTray();

    //If the icon is a file
    Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
    //Alternative (if the icon is on the classpath):
    //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));

    TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
    //Let the system resize the image if needed
    trayIcon.setImageAutoSize(true);
    //Set tooltip text for the tray icon
    trayIcon.setToolTip("System tray icon demo");
    tray.add(trayIcon);

    trayIcon.displayMessage("Pomodoro Technique", notificationText, MessageType.INFO);
  }
  private static void createWindowsPing(){
    //WinSound
    final Runnable runnable =
        (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
    if (runnable != null) runnable.run();

  }

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    String eingabe = "";

    int state = 0;  // 0 = startMenu, 1 = learnSession, 2 = Options
    while(true) {
      if(state == 0)
        state = startMenu(scanner);
      else if(state == 1)
        state = learnSession();
      else if(state == 2)
        state = optionsMenu(scanner);
      else
        break;
    }
    System.out.println(state);
    clearSpace();
    System.out.println("Vielen Dank, dass Sie Pomodoro LearnTimer verwenden.\nBis zum naechsten mal");
    System.exit(0);


  }
  private static void printSperationLine(){
    System.out.println(	 "\t -+-+-+-+-+-+-+-+-+-+-+-+");
  }

  private static void clearSpace(){ //Just to print some empty lines tpo seperate the earlier commands from the new menu
    for(int i = 0; i < 4; i++){
      System.out.println("");
    }
  }
}
