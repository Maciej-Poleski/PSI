/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.Socket;

/**
 * @author krawczyk
 */
public class NioKlient2013 extends Thread {
    String adresSerwera = "127.0.0.1";
    int portSerwera = 12345;
    InputStream zSerwera = null;
    BufferedReader zSerweraBR = null;
    OutputStream naSerwer = null;
    Socket gniazdoKlienta = null;
    BufferedReader stdin = null;


    NioKlient2013() {
        try {
            gniazdoKlienta = new Socket(adresSerwera, portSerwera);
        } catch (IOException e) {
            System.out.println("Nie udalo sie nawiazac polaczenia z serwerem...");
            System.out.println(e.toString());
            System.exit(-1);
        }

        try {
            stdin = new BufferedReader(new InputStreamReader(System.in));
            zSerwera = gniazdoKlienta.getInputStream();
            zSerweraBR = new BufferedReader(new InputStreamReader(zSerwera, "UTF-8"));
            naSerwer = gniazdoKlienta.getOutputStream();
        } catch (IOException e) {
            System.out.println("Nie udalo sie pobrac strumienia wejscia i wyjscia soketu klienta ...");
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        NioKlient2013 klient = new NioKlient2013();
        klient.start();
    }

    void wyslij(String str) {
        try {
            naSerwer.write(str.getBytes());
        } catch (Exception e) {
            System.out.println("Wyjatek w wyslij:" + e.toString());
        }
    }

    private void pobierzListeDostepnychPlikow() {
        System.out.println("Lista plikow do pobrania:");
        wyslij("List");
        try {
            String listaPlikow = zSerweraBR.readLine();
            System.out.println(listaPlikow);
        } catch (IOException e) {
            System.out.println("Wyjatek list:" + e.getMessage());
        }

    }

    private void wypiszInstrukcjeProgramu() {
        System.out.println("List - lista plikow na serwerze");
        System.out.println("Get;x - pobiera plik o numerze x");
        System.out.println("Exit - wyjscie z programu");
        System.out.println("Help - pomoc");
    }

    private void pobierzPlik(String s) {
        wyslij(s);
        try {
            String odpowiedz = zSerweraBR.readLine();
            System.out.println("Odpowiedz:" + odpowiedz);
            if (odpowiedz.startsWith("OK")) {
                System.out.println(odpowiedz);
                String[] odp = odpowiedz.split(";");
                int rozmiarPliku = Integer.parseInt(odp[1]);

                int z;
                int i = 0;
                System.out.println("Wypisywanie pliku: " + rozmiarPliku);
                while (i < rozmiarPliku) {
                    z = zSerweraBR.read();
                    System.out.print((char) z);
                    System.out.flush();
                    i++;
                }
                System.out.println("Plik prczeczytany");
            }
        } catch (IOException e) {
            System.out.println("Wyjatek get: " + e.getMessage());
        }
    }

    private void zamknijProgram() {
        wyslij("Exit");
        try {
            gniazdoKlienta.close();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Wyjatek exit:" + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            System.out.print("-->");
            String s = null;
            try {
                s = stdin.readLine();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            if (s.equals("Help")) {
                wypiszInstrukcjeProgramu();
            } else if (s.equals("List")) {
                pobierzListeDostepnychPlikow();
            } else if (s.equals("Exit")) {
                zamknijProgram();
            } else if (s.substring(0, 3).equals("Get")) {
                pobierzPlik(s);
            } else {
                System.out.println("Niepoprawna instrukcja - patrz help");
            }

        }
    }
}
