
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

    while(eingabe.compareTo("0") != 0 && eingabe.compareTo("1") != 0 && eingabe.compareTo("2") != 0){
      System.out.println("Bitte einen validen Wert eingeben");
      eingabe = scanner.nextLine();
    }
    //Because the actual state values are arranged in another way
    if(eingabe.compareTo("0") == 0)
      return 1;
    else if(eingabe.compareTo("1") == 0)
      return 2;
    else
      return -1;
  }

  private static int optionsMenu(Scanner scanner){
    String eingabe;

    clearSpace();
    //printSperationLine();
    System.out.println("     \tOptionsMenu");
    printSperationLine();
    System.out.println("Bitte waehlen Sie eine Option mit Eingabe der entsprechende Zahl aus");
    System.out.println("0) Dauer der Lernsessions bearbeiten");
    System.out.println("1) Dauer der kurzen Pausen bearbeiten");
    System.out.println("2) Dauer der langen Pausen bearbeiten");
    System.out.println("3) Zurück zum Startmenu");

    eingabe = scanner.nextLine();
    while(eingabe.compareTo("0") != 0 && eingabe.compareTo("1") != 0 && eingabe.compareTo("2") != 0 && eingabe.compareTo("3") != 0){
      System.out.println("Bitte geben sie einen validen Wert ein");
      eingabe = scanner.nextLine();
    }

    if(eingabe.compareTo("0") == 0){
      //TODO
    }else if(eingabe.compareTo("1") == 0){
      //TODO
    }else if(eingabe.compareTo("2") == 0){
      //TODO
    }
    return 0;
  }
  private static int learnSession(){
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

    clearSpace();
    System.out.println("Vielen Dank, dass Sie Pomodoro LearnTimer verwenden.\nBis zum naechsten mal");
    System.exit(0);
    //CMD Output
    System.out.println("Arbeitszeit mit 45min beginnt nun");
    //Win10 Notification
    try {
      createNotification("Die Arbeitszeit von 45min beginnt nun");
    }catch(Exception e){
    }
    createWindowsPing();
    while (eingabe.compareTo("-1") != 0) {
      try {
        TimeUnit.SECONDS.sleep(10);
      } catch (Exception e) {
      }
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 10; j++)
          System.out.println("!!");
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
            + " einlegen? y = 1/n = 0. Oder möchten Sie beenden? y = -1");
        pausenZeit = kurzePause;
      } else {
        System.out.println("Möchtest du eine Pause von " + kurzePause
            + " einlegen? y = 1/n = 0. Oder möchten Sie beenden? y = -1");
        pausenZeit = langePause;
      }
      eingabe = scanner.nextLine();
      while(eingabe.compareTo("-1") != 0 && eingabe.compareTo("0") != 0 && eingabe.compareTo("1") != 0){
        eingabe = scanner.nextLine();
      }
      if(eingabe.compareTo("1") == 0){
        System.out.println("Pause beginnt jetzt mit Länge " + pausenZeit);
        try {
          TimeUnit.MINUTES.sleep(pausenZeit);
        } catch (Exception e) {

        }
      }else if(eingabe.compareTo("0") == 0) {
        System.out.println("Du hast die Pause geskippt. Nächste beginnt in 45min");
      }else{
        System.out.println("Das System wird beendet");
        System.exit(1);
      }
    }

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
