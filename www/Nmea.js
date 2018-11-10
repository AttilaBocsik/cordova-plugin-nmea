var exec = require('cordova/exec');

module.exports = {
    testNmea: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'testNmea', [arg0]);
    },
    startNmea: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'startNmea', [arg0]);
    },
    stopNmea: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'stopNmea', [arg0]);
    },
    getNmea: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmea', [arg0]);
    }/*,
    getNmeaGga: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaGga', [arg0]);
    },
    getNmeaGll: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaGll', [arg0]);
    },
    getNmeaGrs: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaGrs', [arg0]);
    },
    getNmeaGsa: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaGsa', [arg0]);
    },
    getNmeaGst: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaGst', [arg0]);
    },
    getNmeaGsv: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaGsv', [arg0]);
    },
    getNmeaRmc: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaRmc', [arg0]);
    },
    getNmeaZda: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaZda', [arg0]);
    },
    getNmeaVtg: function (arg0, success, error) {
        exec(success, error, 'Nmea', 'getNmeaVtg', [arg0]);
    }*/
    
}