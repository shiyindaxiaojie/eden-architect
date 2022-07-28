package org.ylzl.eden.spring.data.influxdb.core;

import com.google.common.collect.Lists;
import liquibase.pro.packaged.T;
import lombok.RequiredArgsConstructor;
import org.influxdb.InfluxDB;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.impl.InfluxDBMapper;

import java.util.List;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class InfluxDBTemplate {

	private static final String DELETE_FROM = "DELETE FROM ";

	private final InfluxDB influxDB;

	private final InfluxDBMapper influxDBMapper;

	public List<T> query(Class<T> clazz) {
		return influxDBMapper.query(clazz);
	}

	public void save(final T model) {
		influxDBMapper.save(model);
	}

	public void save(final List<T> modelList) {
		Class<?> clazz = modelList.get(0).getClass();
		List<Point> pointList = Lists.newArrayListWithCapacity(modelList.size());
		for (T model : modelList) {
			Point point = Point
				.measurementByPOJO(clazz)
				.addFieldsFromPOJO(model)
				.build();
			pointList.add(point);
		}

		BatchPoints batchPoints = BatchPoints.builder().points(pointList).build();
		Measurement measurement = clazz.getAnnotation(Measurement.class);
		influxDB.setDatabase(measurement.database());
		influxDB.setRetentionPolicy(measurement.retentionPolicy());
		influxDB.write(batchPoints);
	}

	public void delete(final T modelList) {
		Class<?> clazz = modelList.getClass();
		Measurement measurement = clazz.getAnnotation(Measurement.class);
		influxDB.query(new Query(DELETE_FROM + measurement.name(), measurement.database()));
	}

	public List<T> query(final String sql, Class<T> clazz) {
		return influxDBMapper.query(new Query(sql), clazz);
	}

	public void delete(final String sql, Class<T> clazz) {
		Measurement measurement = clazz.getAnnotation(Measurement.class);
		influxDB.query(new Query(sql, measurement.database()));
	}
}
