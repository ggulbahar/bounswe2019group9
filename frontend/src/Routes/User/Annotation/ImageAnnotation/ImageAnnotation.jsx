import React, {useState} from 'react';
import {EditableAnnotation, SubjectRect, ConnectorElbow, ConnectorEndDot, Note} from 'react-annotation';
import { connect } from "../../../../Store";

const ImageAnnotation = ({ annotation, isOwner, onSaveAnnotation, setEditingAnnotation }) => {

    const [location, setLocation] = useState({});
    const [isDragging, setIsDragging] = useState(false);

    const title = annotation.displayTitle;

    const body = annotation.displayBody;

    const x = annotation.displayX;

    const y = annotation.displayY;

    const width = -annotation.displayWidth;

    const height = -annotation.displayHeight;

    const handleDragEnd = (nextProps) => {
        setTimeout(() => {
            console.log('drend');
            setIsDragging(false);
        }, 10)
        const difference = [nextProps.x - (x-width), nextProps.y - (y-height), nextProps.width - width, nextProps.height - height]
            .map((x) => Math.abs(x))
            .reduce((a, b) => a + b);
        console.log('difference', difference);
        if (difference > 0.1) {
            console.log('anno', annotation);
            annotation.displayX = nextProps.x + nextProps.width;
            annotation.displayY = nextProps.y + nextProps.height;
            annotation.displayWidth = -nextProps.width;
            annotation.displayHeight = -nextProps.height;
            const nextAnnotation = annotation.clone();
            nextAnnotation.setModified();
            onSaveAnnotation(annotation, nextAnnotation);
        }
        setLocation({ dx: nextProps.dx, dy: nextProps.dy });
    };

    const handleDragStart = () => {
        setIsDragging(true);
    };

    const handleToggle = (...args) => {
        console.log('event click', ...args);
        if (!isOwner || isDragging) {
            console.log('dragging', isDragging)
            return;
        }
        setEditingAnnotation(annotation);
    };

    return (
        <EditableAnnotation
            x={x - width}
            y={y - height}
            dx={40}
            dy={20 -height/2}
            color={"#9610ff"}
            title={title}
            label={body}
            className="show-bg no-select clickable"
            events={{
                onClick: handleToggle
            }}
            onDragStart={handleDragStart}
            onDragEnd={handleDragEnd}
            width={width}
            height={height}
            {...location}
        >
            <SubjectRect fill={"ddddff"} fillOpacity={0.4} editMode={isOwner} />
            <ConnectorElbow>
                <ConnectorEndDot />
            </ConnectorElbow>

            <Note
                align={"middle"}
                orientation={"topBottom"}
                bgPadding={20}
                padding={15}
                titleColor={"#59039c"}
                lineType={"vertical"}
            />
        </EditableAnnotation>
    );
};

export default ImageAnnotation;