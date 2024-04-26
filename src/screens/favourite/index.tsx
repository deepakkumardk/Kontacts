import React, {
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import {StyleSheet, View} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import type {Contact} from 'react-native-contacts';
import {Searchbar, Text} from 'react-native-paper';
import {FlashList} from '@shopify/flash-list';

import {ContactUtils, PermissionUtils} from 'src/utils';
import LargeCardItem from 'src/screens/contacts/components/LargeCardItem';
import {BottomStackScreenProps} from 'src/navigation';

export const FavouriteScreen = ({
  navigation,
}: BottomStackScreenProps<'Favourite'>) => {
  const [contacts, setContacts] = useState<Contact[]>([]);
  const contactsListRef = useRef<Contact[]>([]);

  const [searchQuery, setSearchQuery] = useState('');
  let intervalId = useRef();

  const getContacts = useCallback(async () => {
    const isGranted = await PermissionUtils.requestContactsPermission();
    if (isGranted) {
      const res = (await ContactUtils.getAll()).filter(
        (item) => item.isStarred,
      );
      contactsListRef.current = contacts;
      setContacts(res);
    }
  }, [contacts]);

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
                const res = await ContactUtils.getSearchedContacts(text, true);
                setContacts(res);
              }, 250);
            }}
            value={searchQuery}
            onClearIconPress={() => {
              setContacts(contactsListRef.current);
            }}
          />
        </View>
      ),
    });
  }, [searchQuery]);

  useEffect(() => {
    getContacts();
  }, []);

  const renderItem = useCallback(({item}: any) => {
    return (
      <LargeCardItem
        contact={item}
        onPress={() => ContactUtils.call(item)}
        onIconPress={() =>
          navigation.navigate('ContactDetail', {contact: item})
        }
      />
    );
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <FlashList
        data={contacts}
        keyExtractor={(item: Contact) => item.recordID}
        numColumns={2}
        renderItem={({item}) => renderItem({item})}
        estimatedItemSize={200}
        ListEmptyComponent={
          <View style={styles.emptyView}>
            <Text variant="titleLarge">{'No Contacts found'}</Text>
          </View>
        }
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginHorizontal: 12,
  },
  row: {
    flexDirection: 'row',
    flex: 1,
  },
  searchbar: {
    width: '105%',
  },
  emptyView: {
    flex: 1,
    alignSelf: 'center',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100%',
    width: '100%',
  },
});
