import React from 'react';

import {createDrawerNavigator} from '@react-navigation/drawer';
import {Dashboard} from 'src/screens/dashboard';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Icon} from 'react-native-paper';

import {FavouriteScreen} from 'src/screens/favourite';
import Ionicons from 'react-native-vector-icons/Ionicons';
import {BottomStackParamList, DrawerStackParamList} from 'src/navigation';

const BottomTab = createBottomTabNavigator<BottomStackParamList>();

const Drawer = createDrawerNavigator<DrawerStackParamList>();

export const DrawerStack = () => {
  return (
    <Drawer.Navigator
      screenOptions={{
        gestureHandlerProps: {
          enabled: true,
        },
        headerShown: false,
      }}>
      <Drawer.Screen name="BottomStack" component={BottomStack} />
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
      // @ts-ignore
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
