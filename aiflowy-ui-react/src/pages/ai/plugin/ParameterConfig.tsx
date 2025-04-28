import React, {useState} from 'react';
import {Button, Form, Input, Select, Space, Switch, Table} from 'antd';
import {DeleteOutlined, EditOutlined, PlusOutlined} from '@ant-design/icons';
import './less/parameterConfig.less'

const { Option } = Select;

interface Parameter {
    key: string;
    name: string;
    description: string;
    type: string;
    method: string;
    required: boolean;
    defaultValue: string;
    enabled: boolean;
}

export  const ParameterConfig: React.FC = () => {
    const [form] = Form.useForm();
    const [data, setData] = useState<Parameter[]>([
        {
            key: '1',
            name: 'city',
            description: '城市',
            type: 'String',
            method: 'Query',
            required: true,
            defaultValue: 'beijing',
            enabled: true,
        },
        {
            key: '2',
            name: 'time',
            description: '时间',
            type: 'String',
            method: 'Query',
            required: true,
            defaultValue: '请填写默认值',
            enabled: true,
        },
    ]);

    const [isEditing, setIsEditing] = useState(false);

    const startEditing = () => {
        setIsEditing(true);
    };

    const cancelEditing = () => {
        setIsEditing(false);
    };

    const saveAll = async () => {
        try {
            const values = await form.validateFields();
            const updatedData = data.map(item => ({
                ...item,
                ...values[item.key] // 获取对应行的数据
            }));
            setData(updatedData);
            setIsEditing(false);
        } catch (errInfo) {
            console.log('Validate Failed:', errInfo);
        }
    };

    const handleAdd = () => {
        const newData: Parameter = {
            key: Date.now().toString(),
            name: '',
            description: '',
            type: 'String',
            method: 'Query',
            required: true,
            defaultValue: '',
            enabled: true,
        };
        setData([...data, newData]);
    };

    const handleDelete = (key: string) => {
        setData(data.filter((item) => item.key !== key));
    };

    const columns = [
        {
            title: '参数名称*',
            dataIndex: 'name',
            key: 'name',
            render: (text: string, record: Parameter) => {
                return isEditing ? (
                    <Form.Item
                        name={[record.key, 'name']}
                        initialValue={text}
                        rules={[{ required: true, message: '请输入参数名称' }]}
                    >
                        <Input placeholder="参数名称" />
                    </Form.Item>
                ) : (
                    text
                );
            },
        },
        {
            title: '参数描述*',
            dataIndex: 'description',
            key: 'description',
            render: (text: string, record: Parameter) => {
                return isEditing ? (
                    <Form.Item
                        name={[record.key, 'description']}
                        initialValue={text}
                        rules={[{ required: true, message: '请输入参数描述' }]}
                    >
                        <Input placeholder="参数描述" />
                    </Form.Item>
                ) : (
                    text
                );
            },
        },
        {
            title: '参数类型*',
            dataIndex: 'type',
            key: 'type',
            render: (text: string, record: Parameter) => {
                return isEditing ? (
                    <Form.Item name={[record.key, 'type']} initialValue={text}>
                        <Select style={{ width: 120 }}>
                            <Option value="String">String</Option>
                            <Option value="Number">Number</Option>
                            <Option value="Boolean">Boolean</Option>
                            <Option value="Object">Object</Option>
                            <Option value="Array">Array</Option>
                        </Select>
                    </Form.Item>
                ) : (
                    text
                );
            },
        },
        {
            title: '传入方法*',
            dataIndex: 'method',
            key: 'method',
            render: (text: string, record: Parameter) => {
                return isEditing ? (
                    <Form.Item name={[record.key, 'method']} initialValue={text}>
                        <Select style={{ width: 120 }}>
                            <Option value="Query">Query</Option>
                            <Option value="Body">Body</Option>
                            <Option value="Header">Header</Option>
                            <Option value="Path">Path</Option>
                        </Select>
                    </Form.Item>
                ) : (
                    text
                );
            },
        },
        {
            title: '是否必须',
            dataIndex: 'required',
            width: 90,
            key: 'required',
            render: (text: boolean, record: Parameter) => {
                return isEditing ? (
                    <Form.Item name={[record.key, 'required']} initialValue={text} valuePropName="checked">
                        <Switch />
                    </Form.Item>
                ) : (
                    <Switch checked={text} disabled />
                );
            },
        },
        {
            title: '默认值',
            dataIndex: 'defaultValue',
            key: 'defaultValue',
            render: (text: string, record: Parameter) => {
                return isEditing ? (
                    <Form.Item name={[record.key, 'defaultValue']} initialValue={text}>
                        <Input placeholder="默认值" size="small" />
                    </Form.Item>
                ) : (
                    text
                );
            },
        },
        {
            title: '开启',
            dataIndex: 'enabled',
            key: 'enabled',
            render: (text: boolean, record: Parameter) => {
                return isEditing ? (
                    <Form.Item name={[record.key, 'enabled']} initialValue={text} valuePropName="checked" >
                        <Switch />
                    </Form.Item>
                ) : (
                    <Switch checked={text} disabled />
                );
            },
        },
        {
            title: '操作',
            key: 'operation',
            render: (_: any, record: Parameter) => (
                <Button
                    type="text"
                    icon={<DeleteOutlined />}
                    onClick={() => handleDelete(record.key)}
                />
            ),
        },
    ];

    return (
        <div>
            <Form form={form} component={false} >
                <Table
                    className="custom-table"
                    bordered
                    dataSource={data}
                    columns={columns}
                    pagination={false}
                    rowKey="key"
                />
            </Form>
            <Space style={{ marginTop: 16 }}>
                {/*{isEditing ? (*/}
                {/*    <>*/}
                {/*        <Button type="primary" onClick={saveAll}>保存全部</Button>*/}
                {/*        <Button onClick={cancelEditing}>取消</Button>*/}
                {/*    </>*/}
                {/*) : (*/}
                {/*    <Button type="primary" icon={<EditOutlined />} onClick={startEditing}>*/}
                {/*        编辑全部*/}
                {/*    </Button>*/}
                {/*)}*/}
                <Button
                    type="dashed"
                    icon={<PlusOutlined />}
                    onClick={handleAdd}
                >
                    新增参数
                </Button>
            </Space>
        </div>
    );
};