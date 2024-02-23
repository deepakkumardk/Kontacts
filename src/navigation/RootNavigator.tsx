import React from 'react';

import {createStackNavigator} from '@react-navigation/stack';

import {ContactDetail} from 'src/screens/contactDetail';
import {DrawerStack} from './AppStack';
import {RootStackParamList} from './types';

const Stack = createStackNavigator<RootStackParamList>();

export const RootNavigator = () => (
  <Stack.Navigator>
    <Stack.Screen name="App" component={DrawerStack} />
    {/* NOTE: screens that are outside the drawer stack */}
    <Stack.Screen name="ContactDetail" component={ContactDetail} />
  </Stack.Navigator>
);
