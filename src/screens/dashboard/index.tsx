import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import {StyleSheet, TouchableOpacity, View} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import type {Contact} from 'react-native-contacts';
import {Searchbar, useTheme} from 'react-native-paper';
import {FlashList} from '@shopify/flash-list';
import Ionicons from 'react-native-vector-icons/Ionicons';

import {ContactUtils, PermissionUtils} from 'src/utils';
import ListItem from './components/ListItem';
import LargeCardItem from './components/LargeCardItem';

export const Dashboard = ({navigation}: any) => {
  const theme = useTheme();

  const [contacts, setContacts] = useState<Contact[]>([]);
  const contactsListRef = useRef<Contact[]>([]);
  const [itemType, setItemType] = useState<'HORIZONTAL' | 'VERTICAL'>(
    'HORIZONTAL',
  );
  const [searchQuery, setSearchQuery] = useState('');
  let intervalId = useRef();

  const getContacts = useCallback(async () => {
    const isGranted = await PermissionUtils.requestContactsPermission();
    if (isGranted) {
      const res = await ContactUtils.getAll();
      contactsListRef.current = contacts;
      setContacts(res);
    }
  }, []);

  useLayoutEffect(() => {
    navigation.setOptions({
      headerTitle: '',
      headerRight: () => (
        <View style={styles.row}>
          <Searchbar
            style={styles.searchbar}
            placeholder="Search"
            onChangeText={(text: string) => {
              setSearchQuery(text);
              clearTimeout(intervalId.current);
              setTimeout(async () => {
                const res = await ContactUtils.getSearchedContacts(text);
                setContacts(res);
              }, 250);
            }}
            value={searchQuery}
            onClearIconPress={() => {
              setContacts(contactsListRef.current);
            }}
          />
          <TouchableOpacity
            style={styles.typeIcon}
            onPress={() =>
              setItemType((prev) =>
                prev === 'HORIZONTAL' ? 'VERTICAL' : 'HORIZONTAL',
              )
            }>
            <Ionicons
              color={theme.colors.primary}
              size={24}
              name={itemType === 'HORIZONTAL' ? 'reorder-four' : 'grid'}
            />
          </TouchableOpacity>
        </View>
      ),
    });
  }, [itemType, searchQuery]);

  useEffect(() => {
    getContacts();
  }, []);

  const renderItem = useCallback(
    ({item}: any) => {
      return itemType === 'HORIZONTAL' ? (
        <LargeCardItem
          contact={item}
          onPress={() => ContactUtils.call(item)}
          onIconPress={() => navigation.navigate('ContactDetail', item)}
        />
      ) : (
        <ListItem
          contact={item}
          onCallPress={() => ContactUtils.call(item)}
          onPress={() => navigation.navigate('ContactDetail', item)}
          onIconPress={() => navigation.navigate('ContactDetail', item)}
        />
      );
    },
    [itemType],
  );

  return (
    <SafeAreaView style={styles.container}>
      <FlashList
        data={contacts}
        key={itemType}
        keyExtractor={(item: Contact) => item.recordID}
        numColumns={itemType === 'HORIZONTAL' ? 2 : 1}
        renderItem={({item}) => renderItem({item})}
        estimatedItemSize={200}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    marginHorizontal: 12,
    flex: 1,
  },
  row: {
    justifyContent: 'space-between',
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  searchbar: {
    width: '96%',
  },
  typeIcon: {
    paddingHorizontal: 6,
  },
});
