import React from 'react';

import {createDrawerNavigator} from '@react-navigation/drawer';
import {createStackNavigator} from '@react-navigation/stack';
import {Dashboard} from 'src/screens/dashboard';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Icon} from 'react-native-paper';

import {FavouriteScreen} from 'src/screens/favourite';
import {ContactDetail} from 'src/screens/contactDetail';
import Ionicons from 'react-native-vector-icons/Ionicons';

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
      <Stack.Screen
        options={{headerShown: true}}
        name="ContactDetail"
        component={ContactDetail}
      />
    </Drawer.Navigator>
  );
};

export const BottomStack = () => (
  <BottomTab.Navigator>
    <BottomTab.Screen
      name="Favourite"
      component={FavouriteScreen}
      options={{
        title: 'Favourite',
        tabBarIcon: ({focused, color, size}) => (
          <Icon
            color={color}
            source={focused ? 'heart' : 'cards-heart-outline'}
            size={size}
          />
        ),
      }}
    />
    <BottomTab.Screen
      name="Dashboard"
      component={Dashboard}
      options={{
        title: 'Contacts',
        tabBarIcon: ({focused, color, size}) => (
          <Ionicons
            color={color}
            name={focused ? 'call' : 'call-outline'}
            size={size}
          />
        ),
      }}
    />
    <BottomTab.Screen
      name="History"
      component={Dashboard}
      options={{
        title: 'Recents',
        tabBarIcon: ({focused, color, size}) => (
          <Icon
            color={color}
            source={focused ? 'history' : 'history'}
            size={size}
          />
        ),
      }}
    />
  </BottomTab.Navigator>
);

export const AppStack = () => (
  <Stack.Navigator>
    <Stack.Screen name="ContactDetail" component={ContactDetail} />
  </Stack.Navigator>
);
