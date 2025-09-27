module ca.qc.bdeb.sim.tp1 {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.qc.bdeb.sim.tp1 to javafx.fxml;
    exports ca.qc.bdeb.sim.tp1;
}
