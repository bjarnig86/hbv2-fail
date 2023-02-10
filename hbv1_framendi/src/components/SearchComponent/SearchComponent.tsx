import { useState, useEffect } from 'react';
import './searchcomponent.scss';
import { authSelector } from '../../stores/auth.slice';
import { useSelector } from 'react-redux';

import {
  Form,
  Input,
  Button,
  Radio,
  Switch,
  DatePicker,
  InputNumber,
  Select,
  Upload,
  Result,
  Tooltip,
  Typography,
  List,
  message,
  Modal,
} from 'antd';
import { UserOutlined } from '@ant-design/icons';
import Avatar from 'antd/lib/avatar/avatar';

type Props = {};

const pseudo = [{}, {}, {}, {}, {}, {}, {}, {}, {}];

interface DaycareWorker {
  address: string;
  children: [];
  experienceInYear: Number;
  firstName: string;
  fullName: string;
  id: Number;
  lastName: string;
  location: string;
  locationCode: Number;
  mobile: string;
  ssn: string;
  freeSpots: Number;
}

interface Location {
  locationCode: string;
  locationName: string;
}

const link = 'http://localhost:8080';
// const link = 'https://dashboard.heroku.com/apps/hbv1-db';

export const SearchComponent = (props: Props) => {
  const [form] = Form.useForm();
  const [applicationForm] = Form.useForm();
  const [data, setData] = useState<DaycareWorker[] | []>([]);
  const [locations, setLocations] = useState<Location[] | []>([]);
  const [location, setLocation] = useState<string | undefined>(undefined);
  const [loading, setLoading] = useState<boolean>(false);
  const [modal, setModal] = useState<boolean>(false);
  const [selectedChild, setSelectedChild] = useState<Number | undefined>();

  const { isLoggedIn, children, userId, type } = useSelector(authSelector);

  useEffect(() => {
    fetchDaycareWorkers();
  }, [location]);

  useEffect(() => {
    const fetchLoactions = async () => {
      setLoading(true);
      const result = await fetch(`${link}/api/locations`);

      if (!result.ok) {
        console.error('Villa!');
      } else {
        const json = await result.json();
        setLocations(json);
      }
      setLoading(false);
    };

    fetchLoactions();
    setLoading(true);
  }, []);

  const fetchDaycareWorkers = async () => {
    setLoading(true);
    const daycareWorkers = await fetch(
      `${link}/api/daycareworkers${location ? `?locationCode=${location}` : ''}`
    );

    if (daycareWorkers.ok && daycareWorkers.status !== 204) {
      const json = await daycareWorkers.json();
      console.log('json', json);
      setData(json);
    } else {
      setData([]);
    }
    setLoading(false);
  };

  const handleSelectChild = (e: Number) => {
    setSelectedChild(e);
  };

  const handleOnFinish = async (values: { location: string }) => {
    const { location } = values;
    setLocation(location);
  };

  const addChildToWaitingList = async (daycareWorkerId: Number) => {
    console.log(daycareWorkerId);
  };

  const onFinish = async (values: any) => {
    const body = {
      daycareWorkerId: values.daycareWorkerId,
      parentId: userId,
      childId: values.childId,
    };

    console.log(values);

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    };

    const result = await fetch(`/api/daycareworker/apply`, options);

    if (!result.ok) {
      message.error('Eitthvað fór úrskeiðis í umsókninni!');
    } else {
      const json = await result.json();
      console.log(json);
      message.success('Umsókn móttekin, jibbí!');
      setSelectedChild(undefined);
    }
    setLoading(false);
  };

  const apply = async (daycareWorkerId: Number) => {
    setSelectedChild(daycareWorkerId);
    setLoading(true);
    if (!isLoggedIn) {
      message.error('Notandi ekki skráður inn');
      setLoading(false);
      return;
    }

    if (!children || children?.length < 1) {
      message.error('Vinsamlegast skráðu barn til að sækja um');
      setLoading(false);
      return;
    }

    Modal.confirm({
      title: 'Veldu barn/börn',
      content: (
        <>
          <Form
            form={applicationForm}
            layout="vertical"
            name="application"
            onFinish={onFinish}
          >
            <Form.Item name="childId">
              <Select
                style={{ width: '200px' }}
                optionFilterProp="children"
                size="large"
              >
                {children?.map<any>((child): any => {
                  return (
                    <Select.Option
                      value={child['id']}
                    >{`${child['firstName']} ${child['lastName']}`}</Select.Option>
                  );
                })}
              </Select>
            </Form.Item>
            <Form.Item name="daycareWorkerId" hidden>
              <Input style={{ display: 'none' }} />
            </Form.Item>
          </Form>
        </>
      ),
      onOk() {
        applicationForm.setFieldValue('daycareWorkerId', daycareWorkerId);
        applicationForm.submit();
      },
      onCancel() {
        setLoading(false);
        console.info('Hætt við');
      },
    });
  };

  return (
    <div className="searchComponent">
      <div className="searchContentContainer">
        <div className="searchBox">
          <Form
            method="POST"
            onFinish={handleOnFinish}
            form={form}
            layout="horizontal"
          >
            <Form.Item name="location" label="Leita eftir staðsetningu: ">
              <Select
                loading={loading}
                showSearch
                optionFilterProp="children"
                size="large"
                style={{ maxWidth: '200px' }}
                placeholder="Everywhere"
                allowClear
              >
                {locations.map((district, i) => {
                  return (
                    <Select.Option
                      key={`option-town-${i}`}
                      value={district.locationCode}
                    >
                      {`${district.locationCode} - ${district.locationName}`}
                    </Select.Option>
                  );
                })}
              </Select>
            </Form.Item>

            <Form.Item className="btnContainer">
              <Button
                disabled={loading}
                loading={loading}
                className="submitBtn"
                type="primary"
                htmlType="submit"
              >
                Leita
              </Button>
            </Form.Item>
          </Form>
        </div>

        <div className="resultBox">
          <div className="daycareWorkers">
            {loading ? (
              <List
                itemLayout="horizontal"
                dataSource={pseudo}
                // loading={loading}
                renderItem={(item) => (
                  <List.Item className="dcwListItem">
                    <List.Item.Meta
                      avatar={<Avatar icon={<UserOutlined />} />}
                      title={
                        <div
                          style={{
                            height: '8px',
                            border: 'solid #e3e3e3 10px',
                            borderRadius: '20px',
                          }}
                        ></div>
                      }
                      style={{ paddingLeft: '14px' }}
                    />
                    {type === 'dcw' ? (
                      <></>
                    ) : (
                      <div style={{ padding: '20px' }}>
                        <Button disabled type="dashed">
                          Sækja um
                        </Button>
                      </div>
                    )}
                  </List.Item>
                )}
              />
            ) : (
              <List
                itemLayout="horizontal"
                dataSource={data}
                // loading={loading}
                renderItem={(item) => (
                  <List.Item className="dcwListItem">
                    <List.Item.Meta
                      avatar={<Avatar icon={<UserOutlined />} />}
                      title={`${item.fullName}`}
                      style={{ paddingLeft: '14px' }}
                      description={
                        <>
                          <p>{`${
                            item.address || 'Ekkert heimilisfang skráð'
                          } | ${
                            item.locationCode + ' ' + item.location || ''
                          } | Laus pláss: ${item.freeSpots}`}</p>
                        </>
                      }
                    />
                    <div style={{ padding: '20px' }}>
                      <Button type="dashed" onClick={() => apply(item.id)}>
                        Sækja um
                      </Button>
                      {item.freeSpots <= 0 && (
                        <Button
                          type="dashed"
                          onClick={() => addChildToWaitingList(item.id)}
                        >
                          Skrá á biðlista
                        </Button>
                      )}
                    </div>
                  </List.Item>
                )}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
