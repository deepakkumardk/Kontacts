import React from 'react';

import {createDrawerNavigator} from '@react-navigation/drawer';
import {Contacts} from 'src/screens/contacts';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {Icon, TouchableRipple, useTheme} from 'react-native-paper';

import {FavouriteScreen} from 'src/screens/favourite';
import Ionicons from 'react-native-vector-icons/Ionicons';
import {BottomStackParamList, DrawerStackParamList} from 'src/navigation';
import {useNavigation} from '@react-navigation/native';
import {CustomDrawer} from 'src/navigation/components/CustomDrawer';

const BottomTab = createBottomTabNavigator<BottomStackParamList>();

const Drawer = createDrawerNavigator<DrawerStackParamList>();

export const DrawerStack = () => {
  return (
    <Drawer.Navigator
      drawerContent={(props) => <CustomDrawer {...props} />}
      screenOptions={{
        gestureHandlerProps: {
          enabled: true,
        },
        headerShown: false,
        swipeEnabled: true,
      }}>
      <Drawer.Screen name="BottomStack" component={BottomStack} />
    </Drawer.Navigator>
  );
};

export const BottomStack = () => {
  const theme = useTheme();
  const navigation = useNavigation();

  return (
    <BottomTab.Navigator
      screenOptions={{
        headerLeft: () => (
          <TouchableRipple
            style={{paddingHorizontal: 8}}
            onPress={() => {
              // @ts-ignore
              navigation.toggleDrawer();
            }}>
            <Ionicons
              color={theme.colors.primary}
              size={32}
              name={'reorder-three'}
            />
          </TouchableRipple>
        ),
      }}>
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
        name="Contacts"
        component={Contacts}
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
        component={Contacts}
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
};
