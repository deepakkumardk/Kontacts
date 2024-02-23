import {BottomTabNavigationProp} from '@react-navigation/bottom-tabs';
import {DrawerNavigationProp} from '@react-navigation/drawer';
import {CompositeScreenProps} from '@react-navigation/native';
import {StackScreenProps} from '@react-navigation/stack';
import {Contact} from 'react-native-contacts';

export type RootStackParamList = {
  App: DrawerNavigationProp<DrawerStackParamList>;

  ContactDetail: {contact: Contact};
};

export type DrawerStackParamList = {
  BottomStack: BottomTabNavigationProp<BottomStackParamList>;
};

export type BottomStackParamList = {
  Favourite: undefined;
  Dashboard: undefined;
  History: undefined;
};

export type AppScreenProps<T extends keyof RootStackParamList> =
  StackScreenProps<RootStackParamList, T>;

export type BottomStackScreenProps<T extends keyof BottomStackParamList> =
  CompositeScreenProps<
    StackScreenProps<BottomStackParamList, T>,
    StackScreenProps<RootStackParamList>
  >;
