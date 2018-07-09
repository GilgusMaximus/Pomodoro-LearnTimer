
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
  //Zeiten die zur Laufzeit geändert werden können
  private static long arbeitsZeit = 45;
  private static long kurzePause = 5;
  private static long langePause = 10;
  private static long arbeitsIntervallCounter = 0;
  //Standard Zeiten
  private static final long arbeitsZeitStandard = 45;
  private static final long kurzePauseStandard = 5;
  private static final long langePauseStandard = 10;

  private static void createNotification(String notificationText) throws AWTException, MalformedURLException{
    //Erzeugt eine neue Windows 10 Notification
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

    //Prüt die Eingabe solange bis 0, 1 oder 2 engeben wurde
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

    //Die Werte in Main sind noch aus einem früheren Patch anders angeordnet, daher müssen wir den return Wert noch richtig konvertieren
    if(eingabe.compareTo("0") == 0)
      return 1; //leanSessiom
    else if(eingabe.compareTo("1") == 0)
      return 2; //OptionMenü
    else if(eingabe.compareTo("2") == 0)
      return -1;  //Programm beenden
    else
      return 0; //und wenn doch etwas schiefgehen sollte, fangen wi mit 0 für Startmenü ab und sind weiterhin im Startmenü
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
    System.out.println("4) Zurueck zum Startmenu");

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

    //einfach nur Ausgabe des jeweiligen Parameters der als nächstes verändert wird, und dann entsprechende ANpassung des RuntimeValues
    if(eingabe.compareTo("0") == 0){
      System.out.println("Bitte neue Zeit in ganzen Minuten eingeben: ");
        while(!scanner.hasNextInt()){
          scanner.next();
          System.out.println("Bitte einen ganzen Wert eingeben");
        }
        arbeitsZeit = scanner.nextInt();
        System.out.println("Ihre neue Arbeitszeit betraegt " + arbeitsZeit +"min");
    }else if(eingabe.compareTo("1") == 0){
      System.out.println("Bitte neue Zeit in ganzen Minuten eingeben: ");
      while(!scanner.hasNextInt()){
        scanner.next();
        System.out.println("Bitte einen ganzen Wert eingeben");
      }
      kurzePause = scanner.nextInt();
      System.out.println("Ihre neue kurze Pause betraegt " + kurzePause + "min");
    }else if(eingabe.compareTo("2") == 0){
      System.out.println("Bitte neue Zeit in ganzen Minuten eingeben: ");
      while(!scanner.hasNextInt()){
        scanner.next();
        System.out.println("Bitte einen ganzen Wert eingeben");
      }
      langePause = scanner.nextInt();
      System.out.println("Ihre neue lange Pause betraegt " + langePause + "min");
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
      while(scanner.hasNext()){
        scanner.next();
      }
      createWindowsPing();
      arbeitsIntervallCounter = arbeitsIntervallCounter +1; //damit wir wissen wann eine lange Pause ansteht (womöglich noch editierbar machen)
      long pausenZeit = 0;
      if(arbeitsIntervallCounter == 3) {
        System.out.println("Moechtest du eine Pause von " + langePause
                + " einlegen? y = 1/n = 0. Oder moechten Sie zum Startmenu? y = 2. Oder moechten Sie beenden? y = -1");
        pausenZeit = kurzePause;
        arbeitsIntervallCounter = 0;
      } else {
        System.out.println("Moechtest du eine Pause von " + kurzePause
                + " einlegen? y = 1/n = 0. Oder moechten Sie zum Startmenu? y = 2. Oder moechten Sie beenden? y = -1");
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
        System.out.println("Pause beginnt jetzt mit Laenge " + pausenZeit);
        try {
          TimeUnit.MINUTES.sleep(pausenZeit);
        } catch (Exception e) {

        }
      }else if(eingabe.compareTo("0") == 0) {
        System.out.println("Du hast die Pause geskippt. Naechste beginnt in "+ arbeitsZeit + "min");
      }else if(eingabe.compareTo("2") == 0) {
        System.out.println("Kehre zum Startmenu zurueck...");
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
    while(true) { //Endlosschleife, durch welche wir einfach immer wieder die States wechseln.
      if(state == 0)
        state = startMenu(scanner);
      else if(state == 1)
        state = learnSession();
      else if(state == 2)
        state = optionsMenu(scanner);
      else  //Nur wenn im Startmenü/Pausenmenü eine -1 eingeben wird, durchbrechen wir die Endlosschleife und beenden das Programm
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

  private static void clearSpace(){ //Just to print some empty lines too seperate the earlier commands from the new menu
    for(int i = 0; i < 4; i++){
      System.out.println("");
    }
  }
}
