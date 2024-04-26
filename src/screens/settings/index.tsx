import React, {useEffect, useState} from 'react';
import {SafeAreaView, StyleSheet, View} from 'react-native';
import {Switch, Text} from 'react-native-paper';
import {AppScreenProps} from 'src/navigation';
import {LocalStorage} from 'src/utils';

const SETTING_DATA = [
  {
    key: 'SAVE_LAST_STATE',
    title: 'Save Last State',
    subtitle:
      'Every time app opens it will remember the last opened page of the bottom menu.',
  },
  {
    key: 'SWIPE_ACTION',
    title: 'Enable Swipe To Call/Message',
    subtitle: 'Swipe left for messages and right for calls for row items',
  },
];

const SETTING_DATA_KEY = 'SETTING_DATA';

const Settings = ({}: AppScreenProps<'Settings'>) => {
  const [settingData, setSettingData] = useState<any>({});

  useEffect(() => {
    const values = LocalStorage.get(SETTING_DATA_KEY);
    setSettingData(values);
  }, []);

  // eslint-disable-next-line react/no-unstable-nested-components
  const SettingItem = ({key, title, subtitle, switchValue}: any) => {
    const onValueChange = () => {
      setSettingData((prev: any) => {
        const next = {
          ...prev,
          [key]: !prev[title],
        };
        LocalStorage.set(SETTING_DATA_KEY, next);
      });
    };
    return (
      <View style={styles.row}>
        <View>
          <Text variant="bodyLarge">{title}</Text>
          <Text variant="labelSmall">{subtitle}</Text>
        </View>
        <Switch value={switchValue} onValueChange={onValueChange} />
      </View>
    );
  };
  return (
    <SafeAreaView>
      {SETTING_DATA.map((item) => (
        <SettingItem {...item} switchValue={settingData[item.title]} />
      ))}
    </SafeAreaView>
  );
};

export default Settings;

const styles = StyleSheet.create({
  row: {
    flex: 1,
    flexDirection: 'row',
  },
});
