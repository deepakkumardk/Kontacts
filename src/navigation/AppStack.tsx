import React from 'react';

import {createDrawerNavigator} from '@react-navigation/drawer';
import {createStackNavigator} from '@react-navigation/stack';
import {Dashboard} from 'src/screens/dashboard';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';

const Stack = createStackNavigator();
const BottomTab = createBottomTabNavigator();

const Drawer = createDrawerNavigator();

export const DrawerStack = () => {
  return (
    <Drawer.Navigator
      screenOptions={{
        gestureHandlerProps: {
          enabled: true,
        },
        headerShown: false,
      }}>
      <Drawer.Screen name="App" component={BottomStack} />
    </Drawer.Navigator>
  );
};

export const BottomStack = () => (
  <BottomTab.Navigator>
    <BottomTab.Screen name="Dashboard" component={Dashboard} />
    <BottomTab.Screen name="Dashboard1" component={Dashboard} />
    <BottomTab.Screen name="Dashboard2" component={Dashboard} />
  </BottomTab.Navigator>
);

export const AppStack = () => (
  <Stack.Navigator>
    <Stack.Screen name="Dashboard" component={Dashboard} />
  </Stack.Navigator>
);
