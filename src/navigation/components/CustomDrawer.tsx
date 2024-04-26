import React from 'react';

import {SafeAreaView} from 'react-native-safe-area-context';
import {Drawer} from 'react-native-paper';
import {DrawerContentScrollView} from '@react-navigation/drawer';
import {useNavigation} from '@react-navigation/native';
import {openContactForm} from 'react-native-contacts';

export const CustomDrawer = ({...rest}: any) => {
  const navigation = useNavigation();

  return (
    <SafeAreaView>
      <DrawerContentScrollView {...rest}>
        <Drawer.Item
          label="Settings"
          onPress={() => {
            // @ts-ignore
            navigation.navigate('Settings');
          }}
        />
        <Drawer.Item
          label="Create New Contact"
          onPress={() => {
            openContactForm({});
          }}
        />
      </DrawerContentScrollView>
    </SafeAreaView>
  );
};
