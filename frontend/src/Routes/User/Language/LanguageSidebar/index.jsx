import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import {Link, useParams, useLocation} from 'react-router-dom';
import {getGrade} from '../../../../Api/Grade';
import { connect, storeType } from '../../../../Store';
import {GradesHelper, ExercisesHelper, LanguagesHelper} from '../../../../Helpers';
import {Menu, Tag, Descriptions, Skeleton, Icon} from 'antd';

const routes = [
  { route: 'main', name: 'Main' },
  { route: 'proficiency-exam', name: 'Proficiency Exam' },
];

const LanguageSidebar = ({ store }) => {
  const {language: languageName} = useParams();
  const { pathname } = useLocation();
  const { languageId } = LanguagesHelper.nameToLanguage(languageName);

  const [grade, setGrade] = useState();

  const currentRoute = pathname.split('/').slice(2).filter((x) => x).join('/') || 'main';

  useEffect(() => {
    if (languageId) {
      getGrade({userId: store.userId, languageId}).then((response) => {
        const {data = {}} = (response.data || {});
        setGrade((data || {}).grade || 0);
      }).catch(console.log)
    }
  }, [languageName]);

  let str_grade = GradesHelper.numGradeToStrGrade(grade);

  return (
    <div>
      <div style={styles.header}>
        <Skeleton loading={!grade} title={false}>
          <Descriptions title={languageName}>
            <Descriptions.Item label={'Grade'}>
              <Tag color={'#87d068'}>{str_grade}</Tag>
            </Descriptions.Item>
          </Descriptions>
        </Skeleton>
      </div>
      <Menu selectedKeys={[currentRoute]} mode="inline">
        {
          routes.map(({ name, route }) => (
            <Menu.Item key={route}>
              { name }
              <Link to={`/${languageName}/${route}`} />
            </Menu.Item>
          ))
        }
        <Menu.SubMenu
          key="exercises"
          title={
            <span>
              <Icon type="reconciliation" />
              <span>Exercises</span>
            </span>
          }
        >
          { ExercisesHelper.exerciseTypes.map(({ typeId, route, name }) => (
            <Menu.Item key={route}>
              { name }
              <Link to={`/${languageName}/exercises/${route}`} />
            </Menu.Item>
          ))}
        </Menu.SubMenu>
      </Menu>
    </div>
  );
};

const styles = {
  header: {
    margin: '24px 12px'
  }
};

LanguageSidebar.propTypes = {
  store: storeType
};

export default connect(LanguageSidebar);
