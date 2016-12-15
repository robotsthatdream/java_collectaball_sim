# Collect A Ball java simulator

## Description

This project is a simulator with a 2D scenario where can be placed different objects an robots, configuring them by a XML configuration file. It's main elements are:

### Scenario

Represents the scenario where the objects and robots are placed and some events happens. There must be functions to restore the scenario for different trials of the robot (reset state of some objects, rewards robots, relocated objects, etc.)
There is one implementation (CollectBallWithDoorScenario) that represents the scenario used for MotivEn experiments, where the robot has to push a button to open a door, so it sees an can reach now a ball. Then it can get the ball and deliver it in the blue box to get a reward.

### Robot

There is one implementation for a robot that can be connected to the MDB. The sensors an actuators of this robot are associated to the sensors and actuators of the MDB through the MDB configuration file. By TCP/IP socket connections, robot sends sensorization to MDB and receives actuations from it.

### Objects

Some objects were implemented (and its graphical representation with a class finished in *ToPaint):
* Red ball: Represents a ball that is a "loadable object" so the robot caught it automatically when they are pretty near.
* Blue box: Represents a box where the robot delivers the ball, obtaining a reward from it.
* Wall with door : Represents a wall to be placed in the scenario and has a door in it.
* Green button: Represents a boton that is automatically pushed when it and the robot are pretty near. It is associated to another object for which it can change its state. In this example, the green button is linked to the door so the door opens when it pushes the button.

## USAGE

The executable program is set up to run the scenario with a robot that is connected to MDB. For this, two configuration files are needed (samples in the config folder): The collect a ball experiment config file and the MDB config file. To run the program:

```bash 
CollectABall.jar -x -mdb config/CoinABall-Config.xml config/MDB-Config.xml
```
The program will be waiting for the MDB execution so the both programs are connected. After this connection, the scenario window will appear. The MDB must be run with the same MDB config file used in CollectABall.




